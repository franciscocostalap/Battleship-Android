package com.example.battleshipmobile.battleship.service.lobby

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
class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentUrl: URL
) : LobbyService {

    companion object {
        private const val QUEUE_REL = "queue"
        private const val CANCEL_QUEUE_REL = "cancelQueue"
        private const val SELF = "self"
        private const val USER_HOME_ERR_MESSAGE = "User home is not set"
        private const val QUEUE_ERR_MESSAGE = "Must enter a queue first"
        private const val PROPERTIES_REQUIRED = "Response has no properties"
    }

    /**
     * Siren node entities
     */
    private var userHomeEntity: SirenEntity<Nothing>? = null
    private var lobbyStateEntity: SirenEntity<LobbyInformation>? = null

    /**
     * Queues up a user to play returning the lobby information.
     *
     * @param userToken token of the user performing the action
     * @return [LobbyInformation]
     */
    override suspend fun enqueue(userToken: String): LobbyInformation {
        val result = buildAndSendRequest<LobbyInformation>(
            client,
            jsonFormatter,
            action = ensureQueueAction(userToken),
            userToken
        )

        lobbyStateEntity = result

        return result.properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)
    }

    /**
     * Gets the lobby information of the one that was requested.
     *
     * @param lobbyID id of the lobby to get
     * @param userToken token of the user performing the action
     * @return [LobbyInformation]
     */
    override suspend fun get(lobbyID: ID, userToken: String): LobbyInformation =
        buildAndSendRequest<LobbyInformation>(
            client,
            jsonFormatter,
            action = ensureLobbyStateLink(),
            userToken
        ).properties ?: throw IllegalStateException(PROPERTIES_REQUIRED)


    /**
     * The user quits from the requested lobby
     *
     * @param lobbyID the id of the lobby
     * @param userToken token of the user performing the action
     */
    override suspend fun cancel(lobbyID: ID, userToken: String) {
        buildAndSendRequest<Unit>(
            client,
            jsonFormatter,
            action = ensureCancelAction(),
            userToken
        )
    }

    /**
     * Fetches and sets the user home if it's not already set
     *
     * @param userToken token of the user performing the action
     */
    private suspend fun fetchUserHomeEntity(userToken: String) {
        if (userHomeEntity != null) return

        val request = buildRequest(parentUrl, token = userToken)

        val responseResult = request.send(client) {
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }

        userHomeEntity = responseResult
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
            sirenEntity = lobbyInformationSirenEntity,
            relation = SELF,
            rootUrl,
            relationType = RelationType.LINK
        )
    }

    /**
     * Ensures that the queue action exists and returns it.
     * Requires that the user home was fetched first.
     *
     * @param userToken token of the user performing the action
     * @return [Action] Queue url and method
     */
    private suspend fun ensureQueueAction(userToken: String): Action {
        fetchUserHomeEntity(userToken)
        val userHomeSirenEntity = userHomeEntity
        require(userHomeSirenEntity != null) { USER_HOME_ERR_MESSAGE }

        return ensureAction(
            sirenEntity = userHomeSirenEntity,
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
            sirenEntity = lobbyStateSirenEntity,
            relation = CANCEL_QUEUE_REL,
            rootUrl,
            relationType = RelationType.ACTION
        )
    }
}