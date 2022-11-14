package com.example.battleshipmobile.battleship.service

import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

data class Action(val url: URL, val method: HttpMethod)

data class ServiceData(
    val client: OkHttpClient,
    val host: String,
    val url: URL,
    val jsonFormatter: Gson,
    val fillServiceUrls: (List<SirenAction>?) -> Unit
)

suspend fun ensureAction(
    _super: ServiceData,
    action: () -> SirenAction?
): Action{

    val actions = _super.url.getActions(_super.client, _super.jsonFormatter)
    if (action() == null) _super.fillServiceUrls(actions)

    val ensuredAction = action() ?: throw IllegalStateException("No action")

    val newUrl = URL(_super.host + ensuredAction.href)
    val method = ensuredAction.method?.let { HttpMethod.valueOf(it) }
        ?: throw IllegalStateException("No register action") // TODO: TIRAR ESTES ERROS MANHOSOS

    return Action(newUrl, method)
}

private suspend fun URL.getActions(client: OkHttpClient, jsonFormatter: Gson): List<SirenAction>? {
    val request = buildRequest(this)
    return request.send(client) {
        handle<SirenEntity<Any>>(
            SirenEntity.getType<Any>().type,
            jsonFormatter
        )
    }.actions
}