package com.example.battleshipmobile.battleship.service.lobby

import android.util.Log
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

class RealLobbyService(
    val client: OkHttpClient,
    val jsonFormatter: Gson,
    val lobbyUrl: String
): LobbyService {

    override suspend fun enqueue(userToken: String): LobbyInformation {
        val request = buildRequest(URL("$lobbyUrl/"), HttpMethod.POST, token= userToken)

        val lobbyInfo = request.send(client){
            handle<SirenEntity<LobbyInformation>>(SirenEntity::class.java, jsonFormatter)
        }.properties

        Log.v("LOBBY INFO", lobbyInfo.toString())
        require(lobbyInfo != null)
        return lobbyInfo
    }

    override suspend fun get(lobbyID: ID): LobbyInformation? {
        val request = buildRequest(URL("${lobbyUrl}/${lobbyID}"), HttpMethod.GET)
        return request.send(client){
            handle<SirenEntity<LobbyInformation>>(SirenEntity::class.java, jsonFormatter)
        }.properties
    }

}