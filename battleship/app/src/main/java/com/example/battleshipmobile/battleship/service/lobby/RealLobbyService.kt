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

class RealLobbyService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val host: String,
    private val homeURL: URL
): LobbyService {

    private val serviceData = ServiceData(client, host, homeURL, jsonFormatter, ::fillServiceUrls)

    private var queueAction: SirenAction? = null
    private var lobbyStateAction: SirenAction? = null

    private suspend fun ensureQueueAction(): Action = ensureAction(serviceData){ queueAction }
    private suspend fun ensureLobbyStateAction(): Action = ensureAction(serviceData){ lobbyStateAction }

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
    override suspend fun get(lobbyID: ID): LobbyInformation? {
        val request = buildRequest(ensureLobbyStateAction())

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
                "lobbyState" -> lobbyStateAction = action
            }
        }
    }

}