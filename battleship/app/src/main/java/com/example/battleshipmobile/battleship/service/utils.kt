package com.example.battleshipmobile.battleship.service

import com.example.battleshipmobile.utils.*
import com.example.battleshipmobile.utils.HttpMethod.*
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import java.net.URI
import java.net.URL

enum class Params(val stringRepresentation: String) {
    LOBBY_ID("lobbyId"),
}

data class Parameter(val name: Params, val value: String)

data class Action(val url: URL, val method: HttpMethod)

data class ServiceData(
    val client: OkHttpClient,
    val root: String,
    val jsonFormatter: Gson,
    val fillServiceUrls: (List<SirenAction>?) -> Unit
)

suspend fun ensureAction(
    _super: ServiceData,
    params: List<Parameter>? = null,
    parentURL: URL,
    token: String? = null,
    action: () -> SirenAction?
): Action{

    val actions = parentURL.getActions(_super.client, _super.jsonFormatter, params, token)
    if (action() == null) _super.fillServiceUrls(actions)

    val ensuredAction = action() ?: throw IllegalStateException("No action")

    val newUrl = URL(_super.root + ensuredAction.href)
    val method = ensuredAction.method?.let { HttpMethod.valueOf(it) }
        ?: throw IllegalStateException("No register action") // TODO: TIRAR ESTES ERROS MANHOSOS*/

    return Action(newUrl, method)
}

private suspend fun URL.getActions(
    client: OkHttpClient,
    jsonFormatter: Gson,
    params: List<Parameter>?,
    userToken: String?
): List<SirenAction>? {
    val newURL = replaceParamPlaceholders(this, params)
    val request = buildRequest(newURL, token= userToken)

    return request.send(client) {
        handle<SirenEntity<Any>>(
            SirenEntity.getType<Any>().type,
            jsonFormatter
        )
    }.actions
}

/**
 * params: [(id1, 1), (id2,2)],
 *
 * path/id1/path/id2 -> path/1/path/2
 */
private fun replaceParamPlaceholders(url: URL, params: List<Parameter>?): URL{
    val stringBuilder = StringBuilder()
    stringBuilder.append(url)
    params?.forEach { parameter ->
        stringBuilder.replace(
            Regex("/${parameter.name.stringRepresentation}"),
            parameter.value
        )
    }
    return URL(stringBuilder.toString())
}

