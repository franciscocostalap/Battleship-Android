package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.play.shotDefinition.GameTurn
import com.example.battleshipmobile.battleship.service.*
import com.example.battleshipmobile.battleship.service.dto.*
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.GameRules
import com.example.battleshipmobile.battleship.service.model.GameStateInfo
import com.example.battleshipmobile.battleship.service.model.State.*
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
) : GameService{

    companion object {
        private const val QUEUE_REL = "queue"
        private const val CANCEL_QUEUE_REL = "cancelQueue"
        private const val LOBBY_STATE_REL = "lobby-state"
        private const val LAYOUT_DEFINITION_REL = "layout-definition"
        private const val SHOT_DEFINITION_REL = "shots-definition"
        private const val GAME_RULES_REL = "game-rules"
        private const val MY_FLEET_REL = "myFleet"
        private const val OPPONENT_FLEET_REL = "opponentFleet"
        private const val FLEET_REL = "fleet"
        private const val SELF = "self"
        private const val USER_HOME_ERR_MESSAGE = "User home entity is not set"
        private const val GAME_STATE_ERR_MSG = "Game state entity is not set"
        private const val QUEUE_ERR_MESSAGE = "Must enter a queue first"
        private const val PROPERTIES_REQUIRED = "Response has no properties"
        private const val GAME_NOT_STARTED= "Game has not started"
        private const val MUST_BE_PLACING_SHIPS = "Game state must be PLACING_SHIPS"
        private const val MUST_BE_PLAYING = "Game state must be PLAYING"
        private const val BOARD_EMBEDDED_LINK_REQUIRED = "This game state requires a board embedded link"
        private const val BOARD_EMBEDDED_ENTITY_REQUIRED =  "Board embedded entity required"
    }

    //TODO make it scalable to multiple games for a user


    /**
     * Siren node entities
     */
    private var userHomeEntity: SirenEntity<Nothing>? = null
    private var lobbyStateEntity: SirenEntity<LobbyInformationDTO>? = null
    private var gameStateEntity: SirenEntity<GameStateInfoDTO>? = null

    /**
     * Producer Scopes of the multiple polling operations
     */
    private var lobbyProducerScope: ProducerScope<LobbyInformation>? = null
    private var gameStateProducerScope: ProducerScope<GameStateInfo>? = null
    private var myBoardProducerScope: ProducerScope<Board>? = null

    /**
     * Queues up a user to play returning the lobby information.
     * @return [LobbyInformationDTO]
     */
    override suspend fun enqueue(): LobbyInformation {
        val result = buildAndSendRequest<LobbyInformationDTO>(
            client,
            jsonFormatter,
            relation = ensureQueueAction(),
        )

        lobbyStateEntity = result
        val gameID = result.properties?.gameID
        // If the player is the second to enqueue
        if (gameID != null)
            fetchGameState()

        return result.properties?.toLobbyInformation()
            ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Gets the lobby information of the one that was requested.
     * @return [LobbyInformationDTO]
     */
    override suspend fun getLobbyInformation(): LobbyInformation {
        val result = buildAndSendRequest<LobbyInformationDTO>(
        client,
        jsonFormatter,
        relation = ensureLobbyStateLink(),
        )

        lobbyStateEntity = result
        val lobbyInformationDTO = result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)

        if(lobbyInformationDTO.gameID != null)
            fetchGameState()

        return lobbyInformationDTO.toLobbyInformation()
    }


    /**
     * Gets the game state information of the game that was requested.
     */
    override suspend fun getGameStateInfo(): GameStateInfo {
        val result = buildAndSendRequest<GameStateInfoDTO>(
            client,
            jsonFormatter,
            relation = ensureGameStateLink(),
        )

        //Update the game state entity with the new game state since it may be different
        gameStateEntity = result

        return result.properties?.toGameStateInfo()
            ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }


    /**
     * Gets the game rules of the game
     */
    override suspend fun getGameRules(): GameRules =
        buildAndSendRequest<GameRulesDTO>(
            client,
            jsonFormatter,
            relation = ensureGameRulesLink(),
        ).properties?.toGameRules()
            ?: throw IllegalStateException(PROPERTIES_REQUIRED)

    /**
     * The user quits from the requested lobby
     */
    override suspend fun cancelQueue() {
        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            relation = ensureCancelAction(),
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
            relation = ensureLayoutDefinitionAction(),
            body = body
        )
    }

    /**
     * Gets the board of one of the players
     * @whichFleet which fleet to get the board from
     * @return [BoardDTO]
     */
    override suspend fun getBoard(whichFleet: GameTurn): Board{
        val relationKey = when(whichFleet){
            GameTurn.MY -> MY_FLEET_REL
            GameTurn.OPPONENT -> OPPONENT_FLEET_REL
        }

        val currentGameStateEntity = gameStateEntity ?: throw IllegalStateException(GAME_STATE_ERR_MSG)
        val embeddedLink = currentGameStateEntity.ensureEmbeddedBoardLink(relationKey)

        val result = buildAndSendRequest<BoardDTO>(
            client,
            jsonFormatter,
            relation = embeddedLink
        )

        return result.properties?.toBoard() ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Makes shots to a board
     * @param shotsDefinitionDTO list of shots to be made
     * @return [BoardDTO] the new board after the shots
     */
    override suspend fun makeShots(shotsDefinitionDTO: ShotsDefinitionDTO){
        val body = jsonFormatter.toJson(shotsDefinitionDTO)
        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            relation = ensureShotDefinitionAction(embedded = false),
            body = body
        )
    }


    /**
     * Keeps polling the game state [GameStateInfoDTO].
     *
     * @return the flow of the game state
     */
    override suspend fun pollGameStateInfo(): Flow<GameStateInfo> {
        return callbackFlow {
            gameStateProducerScope = this
            this.use{
                val startingGameState = gameStateEntity?.properties
                require(startingGameState != null) { GAME_STATE_ERR_MSG }

                do{
                    val gameState = getGameStateInfo()
                    trySend(gameState)
                    delay(3000)
                }while (true)

            }
        }
    }

    /**
     * Keeps polling the lobby information [LobbyInformationDTO].
     *
     * @return the flow of the lobby information
     */
    override suspend fun pollLobbyInformation(): Flow<LobbyInformation> {
        return callbackFlow {
            lobbyProducerScope = this
            this.use{
                //Fast path
                val startingLobbyState = lobbyStateEntity?.properties
                require (startingLobbyState != null) { QUEUE_ERR_MESSAGE }
                if(startingLobbyState.gameID != null)
                    trySend(startingLobbyState.toLobbyInformation())

                do{
                    val lobbyInformation = getLobbyInformation()
                    trySend(lobbyInformation)
                    delay(1500L)
                }while(lobbyInformation.gameID == null)

            }
        }
    }

    override suspend fun pollMyBoard(): Flow<Board> {
        return callbackFlow {
            myBoardProducerScope = this
            this.use {
                do{
                    val board = getBoard(GameTurn.MY)
                    trySend(board)
                    delay(3000L)
                }while(true)
            }
        }
    }

    /**
     * Cancels the polling of the user board
     */
    override suspend fun cancelPollMyBoard() {
        myBoardProducerScope?.close()
        myBoardProducerScope = null
    }

    /**
     * Cancels the polling of the lobby GameState.
     */
    override suspend fun cancelPollingGameState() {
        gameStateProducerScope?.close()
        gameStateProducerScope = null
    }

    /**
     * Cancels the current lobby polling
     */
    override fun cancelLobbyPolling(){
        lobbyProducerScope?.close()
        lobbyProducerScope = null
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

        gameStateEntity = responseResult
    }

    /**
     * Ensures that the lobby state link exists and returns it.
     * Requires that the queue action was performed first.
     *
     * @return [Relation] Lobby state url and method
     */
    private fun ensureLobbyStateLink(): Relation{
        val lobbyInformationSirenEntity = lobbyStateEntity
        require(lobbyInformationSirenEntity != null) { QUEUE_ERR_MESSAGE }
        val relation = if(lobbyInformationSirenEntity.links?.find{ link ->
                link.rel.firstOrNull{ it == LOBBY_STATE_REL } != null } != null)
            LOBBY_STATE_REL
        else
            SELF

        return ensureRelation(
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
     * @return [Relation] Game state url and method
     */
    private fun ensureGameStateLink(): Relation{
        val gameStateSirenEntity = gameStateEntity
        require(gameStateSirenEntity != null) { GAME_STATE_ERR_MSG }

        return ensureRelation(
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
     * @return [Relation] Game rules url and method
     */
    private suspend fun ensureGameRulesLink(): Relation{
        gameStateEntity ?: fetchGameState()
        val gameStateSirenEntity = gameStateEntity
        require(gameStateSirenEntity != null) { GAME_STATE_ERR_MSG }

        return ensureRelation(
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
    * @return [Relation] Queue url and method
     */
    private suspend fun ensureQueueAction(): Relation {
        userHomeEntity = super.fetchParentEntity(client,jsonFormatter,parentURL,userHomeEntity)

        val userHomeSirenEntity = userHomeEntity
        require(userHomeSirenEntity != null) { USER_HOME_ERR_MESSAGE }

        return ensureRelation(
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
     * @return [Relation] Cancel queue url and method
     */
    private fun ensureCancelAction(): Relation {
        val lobbyStateSirenEntity = lobbyStateEntity
        require(lobbyStateSirenEntity != null) { QUEUE_ERR_MESSAGE }

        return ensureRelation(
            parentSirenEntity = lobbyStateSirenEntity,
            relation = CANCEL_QUEUE_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }

    /**
     * Ensures that the layout definition action is available in the game state entity
     *
     * @return [Relation] layout definition action
     */
    private suspend fun ensureLayoutDefinitionAction(): Relation {
        fetchGameState()
        val gameStateEntity = gameStateEntity
        require(gameStateEntity != null){ GAME_STATE_ERR_MSG }

        return ensureRelation(
            parentSirenEntity = gameStateEntity,
            relation = LAYOUT_DEFINITION_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }

    /**
     * Ensures that the shots definition action is available in the game state entity
     *
     * @return [Relation] shots definition action
     */
    private suspend fun ensureShotDefinitionAction(embedded: Boolean): Relation {
        fetchGameState()
        val gameStateEntity = gameStateEntity
        require(gameStateEntity != null){ GAME_STATE_ERR_MSG }
        val state = gameStateEntity.properties?.state
        require(state == PLAYING) { MUST_BE_PLAYING }

        return ensureRelation(
            parentSirenEntity = gameStateEntity,
            relation = SHOT_DEFINITION_REL,
            rootUrl,
            relationType = RelationType.ACTION,
            embeddedInfo = embedded
        )
    }

    /**
     * Ensures that the board embedded link is available in the game state entity
     */
    private fun SirenEntity<GameStateInfoDTO>.ensureEmbeddedBoardLink(rel: String): Relation{
        val entity = entities?.find { subEntity ->
            subEntity as EmbeddedLink
            subEntity.rel.any{ it == rel }
        }

        require(entity != null) { BOARD_EMBEDDED_LINK_REQUIRED }

        entity as EmbeddedLink
        return Relation(URL(rootUrl + entity.href))
    }

}