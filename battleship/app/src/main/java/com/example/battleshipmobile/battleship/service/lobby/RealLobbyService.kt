package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.ServiceData
import com.example.battleshipmobile.battleship.service.ensureAction
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
 * @property parentUrl url that gives access to the requested resources with its siren actions/links
 */
class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentUrl: URL
): LobbyService {

    private val serviceData = ServiceData(client, rootUrl, parentUrl, jsonFormatter, ::fillServiceUrls)

    private var queueAction: SirenAction? = null

    private suspend fun ensureQueueAction(): Action = ensureAction(serviceData){ queueAction }

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
    override suspend fun getLobbyInfo(lobbyID: ID): LobbyInformation? {
        val request = buildRequest(URL("${parentUrl}/lobby/${lobbyID}"))

        return request.send(client){
            handle<SirenEntity<LobbyInformation>>(
                SirenEntity.getType<LobbyInformation>().type,
                jsonFormatter
            )
        }.properties
    }

    private fun fillServiceUrls(actions: List<SirenAction>?) {
        actions?.forEach { action ->
            when (action.name) {
                "queue" -> queueAction = action
            }
        }
    }

}