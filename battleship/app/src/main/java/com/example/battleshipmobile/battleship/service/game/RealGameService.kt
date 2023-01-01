package com.example.battleshipmobile.battleship.service.game

import android.util.Log
import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.service.*
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

/**
 * Lobby Service
 * @property client Http client
 * @property jsonFormatter
 * @property rootUrl api base url used for all endpoints
 * @property parentUrl url that gives access to the requested resources with its siren actions/links
 */
class RealGameService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentUrl: URL
) : GameService {

    companion object {
        private const val QUEUE_REL = "queue"
        private const val CANCEL_QUEUE_REL = "cancelQueue"
        private const val LAYOUT_DEFINITION_REL = "layout-definition"
        private const val SELF = "self"
        private const val USER_HOME_ERR_MESSAGE = "User home is not set"
        private const val QUEUE_ERR_MESSAGE = "Must enter a queue first"
        private const val PROPERTIES_REQUIRED = "Response has no properties"
        private const val ILLEGAL_GAME_STATE = "Game state must be PLACING_SHIPS"
        private const val GAME_STATE_ERR_MSG = "Game state is not set"
        private const val GAME_NOT_STARTED= "Game has not started"
    }

    /**
     * Siren node entities
     */
    private var userHomeEntity: SirenEntity<Nothing>? = null
    private var lobbyStateEntity: SirenEntity<LobbyInformation>? = null
    private var gameStateEntity: SirenEntity<GameStateInfo>? = null

    /**
     * Queues up a user to play returning the lobby information.
     * @return [LobbyInformation]
     */
    override suspend fun enqueue(): LobbyInformation {
        val result = buildAndSendRequest<LobbyInformation>(
            client,
            jsonFormatter,
            action = ensureQueueAction(),
        )

        lobbyStateEntity = result

        return result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Gets the lobby information with the given lobbyID that was requested.
     *
     * @param lobbyID id of the lobby to get
     * @return [LobbyInformation]
     */
    override suspend fun get(lobbyID: ID): LobbyInformation =
        buildAndSendRequest<LobbyInformation>(
            client,
            jsonFormatter,
            action = ensureLobbyStateLink(),
        ).properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)


    /**
     * The user quits from the requested lobby
     *
     * @param lobbyID the id of the lobby
     */
    override suspend fun cancel(lobbyID: ID) {
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
        Log.v("BODY", body)

        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            action = ensureLayoutDefinitionAction(),
            body = body
        )
    }



    /**
     * Fetches and sets the game state entity
     */
    private suspend fun fetchGameState(){
        val lobbyID = lobbyStateEntity?.properties?.lobbyID
                ?: throw IllegalStateException(QUEUE_ERR_MESSAGE)
        val gameState = get(lobbyID)
        val gameID = gameState.gameID ?:
                        throw IllegalStateException(GAME_NOT_STARTED)

        val request = buildRequest(URL("$rootUrl/game/$gameID/state")) //TODO: remove hardcoding
//TODO: add HTTP CACHING on all services
        val responseResult = request.send(client) {
            handle<SirenEntity<GameStateInfo>>(
                SirenEntity.getType<GameStateInfo>().type,
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

        return ensureAction(
            parentSirenEntity = lobbyInformationSirenEntity,
            relation = SELF,
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
        userHomeEntity = super.fetchParentEntity(
            client,
            jsonFormatter,
            parentUrl,
            userHomeEntity
        )
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