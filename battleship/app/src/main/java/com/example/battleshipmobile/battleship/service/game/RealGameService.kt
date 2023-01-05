package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.service.*
import com.example.battleshipmobile.battleship.service.dto.GameRulesDTO
import com.example.battleshipmobile.battleship.service.dto.GameStateInfoDTO
import com.example.battleshipmobile.battleship.service.dto.LobbyInformationDTO
import com.example.battleshipmobile.battleship.service.dto.ShipsInfoDTO
import com.example.battleshipmobile.battleship.service.model.State
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import java.net.URL

/**
 * Lobby Service
 * @property client Http client
 * @property jsonFormatter
 * @property rootUrl api base url used for all endpoints
 * @property parentURL url that gives access to the requested resources with its siren actions/links
 */
class RealGameService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentURL: URL
) : GameService {

    companion object {
        private const val QUEUE_REL = "queue"
        private const val CANCEL_QUEUE_REL = "cancelQueue"
        private const val LOBBY_STATE_REL = "lobby-state"
        private const val LAYOUT_DEFINITION_REL = "layout-definition"
        private const val GAME_RULES_REL = "game-rules"
        private const val SELF = "self"
        private const val USER_HOME_ERR_MESSAGE = "User home entity is not set"
        private const val GAME_STATE_ERR_MSG = "Game state entity is not set"
        private const val QUEUE_ERR_MESSAGE = "Must enter a queue first"
        private const val PROPERTIES_REQUIRED = "Response has no properties"
        private const val GAME_NOT_STARTED= "Game has not started"
        private const val ILLEGAL_GAME_STATE = "Game state must be PLACING_SHIPS"
    }

    //TODO use caching

    /**
     * Siren node entities
     */
    private var userHomeEntity: SirenEntity<Nothing>? = null
    private var lobbyStateEntity: SirenEntity<LobbyInformationDTO>? = null
    private var gameStateEntity: SirenEntity<GameStateInfoDTO>? = null

    /**
     * flow producer scope to allow cancelling
     */
     private var lobbyProducerScope: ProducerScope<LobbyInformationDTO>? = null

    /**
     * Queues up a user to play returning the lobby information.
     * @return [LobbyInformationDTO]
     */
    override suspend fun enqueue(): LobbyInformationDTO {
        val result = buildAndSendRequest<LobbyInformationDTO>(
            client,
            jsonFormatter,
            action = ensureQueueAction(),
        )

        lobbyStateEntity = result
        val gameID = result.properties?.gameID
        // If the player is the second to enqueue
        if (gameID != null)
            fetchGameState()

        return result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Gets the lobby information of the one that was requested.
     * @return [LobbyInformationDTO]
     */
    override suspend fun getLobbyInformation(): LobbyInformationDTO {
        val result = buildAndSendRequest<LobbyInformationDTO>(
        client,
        jsonFormatter,
        action = ensureLobbyStateLink(),
        )

        lobbyStateEntity = result
        val lobbyInformation = result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)

        if(lobbyInformation.gameID != null)
            fetchGameState()

        return lobbyInformation
    }


    /**
     * Gets the game state information of the game that was requested.
     */
    override suspend fun getGameStateInfo(): GameStateInfoDTO {
        val result = buildAndSendRequest<GameStateInfoDTO>(
            client,
            jsonFormatter,
            action = ensureGameStateLink(),
        )

        //Update the game state entity with the new game state since it may be different
        gameStateEntity = result

        return result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }


    /**
     * Gets the game rules of the game
     */
    override suspend fun getGameRules(): GameRulesDTO =
        buildAndSendRequest<GameRulesDTO>(
            client,
            jsonFormatter,
            action = ensureGameRulesLink(),
        ).properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)

    /**
     * The user quits from the requested lobby
     */
    override suspend fun cancel() {
        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            action = ensureCancelAction(),
        )
    }

    /**
     * Places the given ships on the board.
     * @param layout list of ships to be placed
     */
    override suspend fun placeShips(layout: ShipsInfoDTO){
        val body = jsonFormatter.toJson(layout)

        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            action = ensureLayoutDefinitionAction(),
            body = body
        )
    }

    /**
     * Keeps polling the game state [GameStateInfoDTO].
     *
     * @return the flow of the game state
     */
    override suspend fun pollGameStateInfo(): Flow<GameStateInfoDTO> {
        return callbackFlow {
            try {
                val startingGameState = gameStateEntity?.properties
                require(startingGameState != null) { GAME_STATE_ERR_MSG }
                if(startingGameState.state != State.PLACING_SHIPS)
                    trySend(startingGameState)

                do{
                    val gameState = getGameStateInfo()
                    trySend(gameState)
                    delay(3000)
                }while (gameState.state == State.PLACING_SHIPS)

            }catch (e: Exception){
                this.close(e)
            }finally {
                this.close()
            }
        }
    }

    /**
     * Keeps polling the lobby information [LobbyInformationDTO].
     *
     * @return the flow of the lobby information
     */
    override suspend fun pollLobbyInformation(): Flow<LobbyInformationDTO> {
        return callbackFlow {
            try {
                //Fast path
                val startingLobbyState = lobbyStateEntity?.properties
                require (startingLobbyState != null) { QUEUE_ERR_MESSAGE }
                if(startingLobbyState.gameID != null)
                    trySend(startingLobbyState)

                do{
                    val lobbyInformation = getLobbyInformation()
                    trySend(lobbyInformation)
                    delay(1500L)
                }while(lobbyInformation.gameID == null)

            }catch (e: Exception) {
                this.close(e)
            }finally {
                this.close()
            }
        }
    }

    /**
     * Cancels the current polling
     */
    override fun cancelPolling(){
        lobbyProducerScope?.close() //TODO change how to get the scope
    }

    /**
     * Fetches and sets the game state entity
     */
    private suspend fun fetchGameState(){
        val gameID = lobbyStateEntity?.properties?.gameID ?:
                        throw IllegalStateException(GAME_NOT_STARTED)

        val request = buildRequest(URL("$rootUrl/game/$gameID/state"))

        val responseResult = request.send(client) {
            handle<SirenEntity<GameStateInfoDTO>>(
                SirenEntity.getType<GameStateInfoDTO>().type,
                jsonFormatter
            )
        }
        require(responseResult.properties?.state == State.PLACING_SHIPS){ ILLEGAL_GAME_STATE }
        gameStateEntity = responseResult
    }



    /**
     * Ensures that the lobby state link exists and returns it.
     * Requires that the queue action was performed first.
     *
     * @return [Action] Lobby state url and method
     */
    private fun ensureLobbyStateLink(): Action{
        val lobbyInformationSirenEntity = lobbyStateEntity
        require(lobbyInformationSirenEntity != null) { QUEUE_ERR_MESSAGE }
        val relation = if(lobbyInformationSirenEntity.links?.find{ link ->
                link.rel.firstOrNull{ it == LOBBY_STATE_REL } != null } != null)
            LOBBY_STATE_REL
        else
            SELF

        return ensureAction(
            parentSirenEntity = lobbyInformationSirenEntity,
            relation = relation,
            rootUrl,
            relationType = RelationType.LINK
        )
    }

    /**
     * Ensures that the game state link exists and returns it.
     * Requires that the queue action was performed first and the game was already created.
     *
     * @return [Action] Game state url and method
     */
    private fun ensureGameStateLink(): Action{
        val gameStateSirenEntity = gameStateEntity
        require(gameStateSirenEntity != null) { GAME_STATE_ERR_MSG }

        return ensureAction(
            parentSirenEntity = gameStateSirenEntity,
            relation = SELF,
            rootUrl,
            relationType = RelationType.LINK
        )
    }

    /**
     *  Ensures that the game rules link exists and returns it.
     *  Requires that the queue action was performed first and the game was already created.
     *
     * @return [Action] Game rules url and method
     */
    private suspend fun ensureGameRulesLink(): Action{
        gameStateEntity ?: fetchGameState()
        val gameStateSirenEntity = gameStateEntity
        require(gameStateSirenEntity != null) { GAME_STATE_ERR_MSG }

        return ensureAction(
            parentSirenEntity = gameStateSirenEntity,
            relation = GAME_RULES_REL,
            rootUrl,
            relationType = RelationType.LINK
        )
    }


    /**
     * Ensures that the queue action exists and returns it.
     * Requires that the user home was fetched first.
     *
    * @return [Action] Queue url and method
     */
    private suspend fun ensureQueueAction(): Action {
        userHomeEntity = super.fetchParentEntity(client,jsonFormatter,parentURL,userHomeEntity)

        val userHomeSirenEntity = userHomeEntity
        require(userHomeSirenEntity != null) { USER_HOME_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = userHomeSirenEntity,
            relation = QUEUE_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }

    /**
     * Ensures that the cancel queue action exists and returns it.
     * Requires that the queue action was performed first.
     *
     * @return [Action] Cancel queue url and method
     */
    private fun ensureCancelAction(): Action {
        val lobbyStateSirenEntity = lobbyStateEntity
        require(lobbyStateSirenEntity != null) { QUEUE_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = lobbyStateSirenEntity,
            relation = CANCEL_QUEUE_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }

    /**
     * Ensures that the layout definition action is available in the game state entity
     *
     * @return [Action] layout definition action
     */
    private suspend fun ensureLayoutDefinitionAction(): Action {
        fetchGameState()
        val gameStateEntity = gameStateEntity
        require(gameStateEntity != null){ GAME_STATE_ERR_MSG }

        return ensureAction(
            parentSirenEntity = gameStateEntity,
            relation = LAYOUT_DEFINITION_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }
}