package com.example.battleshipmobile.battleship.service

import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.utils.SirenEntity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL


interface AppService{

    suspend fun fetchParentEntity(client: OkHttpClient, jsonFormatter: Gson, parentURL: URL, parentEntity : SirenEntity<Nothing>?) : SirenEntity<Nothing>? {
        if(parentEntity != null) return parentEntity

        val request = com.example.battleshipmobile.battleship.http.buildRequest(
            parentURL
        )
        val responseResult = request.send(client){
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }
        return responseResult
    }
}
