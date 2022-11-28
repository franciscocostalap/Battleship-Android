package com.example.battleshipmobile.battleship.service.lobby

import android.util.Log
import com.example.battleshipmobile.battleship.service.*
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

/**
 * Lobby Service
 * @property client Http client
 * @property jsonFormatter
 * @property rootUrl api base url used in all endpoints
 */
class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
): LobbyService {

    private val serviceData = ServiceData(client, rootUrl, jsonFormatter, ::fillServiceUrls)

    private var queueAction: SirenAction? = null
    private var cancelAction: SirenAction? = null

    private suspend fun ensureQueueAction(): Action = ensureAction(
        serviceData,
        parentURL = URL("$rootUrl/my"),
        action = { queueAction }
    )

    private suspend fun ensureCancelAction(lobbyID: ID, userToken: String): Action = ensureAction(
        serviceData,
        params = listOf(Parameter(Params.LOBBY_ID, lobbyID.toString())),
        parentURL = URL("$rootUrl/lobby/$lobbyID"),
        token = userToken,
        action =  { cancelAction }
    )

    private fun buildRequest(action: Action, userToken: String?= null): Request =
        buildRequest(action.url, action.method, token= userToken)


    /**
     * Queues up a user to play returning the lobby information.
     */
    override suspend fun enqueue(userToken: String): LobbyInformation {
        val request = buildRequest(ensureQueueAction(), userToken)

        val result = request.send(client){
            handle<SirenEntity<LobbyInformation>>(
                SirenEntity.getType<LobbyInformation>().type,
                jsonFormatter
            )
        }

        return result.properties ?: throw IllegalStateException("No properties in response")
    }

    /**
     * Gets the lobby information of the one that was requested.
     */
    override suspend fun get(lobbyID: ID, userToken: String): LobbyInformation? {
        val request = buildRequest(URL("${rootUrl}/lobby/${lobbyID}"), token= userToken)

        return request.send(client){
            handle<SirenEntity<LobbyInformation>>(
                SirenEntity.getType<LobbyInformation>().type,
                jsonFormatter
            )
        }.properties
    }

    /**
     *
     */
    override suspend fun cancel(lobbyID: ID, userToken: String) {
        val request = buildRequest(
            ensureCancelAction(lobbyID, userToken),
            userToken
        )

        return request.send(client){
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }
    }

    private fun fillServiceUrls(
        actions: List<SirenAction>?
    ) {
        actions?.forEach { action ->
            when (action.name) {
                "queue" -> queueAction = action
                "cancelQueue" -> cancelAction = action
            }
        }
    }
}