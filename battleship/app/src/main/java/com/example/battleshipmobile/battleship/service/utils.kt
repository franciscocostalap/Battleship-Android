package com.example.battleshipmobile.battleship.service

import android.util.Log
import com.example.battleshipmobile.battleship.service.RelationType.*
import com.example.battleshipmobile.utils.*
import com.example.battleshipmobile.utils.HttpMethod.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

enum class RelationType { ACTION, LINK }
data class Action(val url: URL, val method: HttpMethod = GET)

private const val HTTP_METHOD_REQUIRED = "HTTP method required"
private const val LINK_NOT_FOUND = "Entity has no link with the rel: "
private const val ACTION_NOT_FOUND = "Entity has no action with the rel: "

/**
 * Ensures that the action or link is present in the entity
 *
 * @param sirenEntity entity to check
 * @param relation relation to check
 * @param rootUrl api base url used for all endpoints
 * @param relationType type of connection to check: action or link
 * @return [Action] the action or link url and method
 */

fun <T> ensureAction(
    sirenEntity: SirenEntity<T>,
    relation: String,
    rootUrl: String,
    relationType: RelationType
): Action =
    if (relationType == ACTION) {
        val sirenAction =
            sirenEntity.actions?.find { it.name == relation } ?:
            throw UnresolvedActionException(ACTION_NOT_FOUND + relation)

        buildAction(rootUrl, sirenAction.href.toString(), sirenAction.method)
    } else {
        val sirenLink =
            sirenEntity.links?.find { it.rel.contains(relation) } ?:
            throw UnresolvedActionException(LINK_NOT_FOUND + relation)

        buildAction(rootUrl, sirenLink.href.toString())
    }

/**
 * Builds an [Action]
 *
 * @param root api base url used for all endpoints
 * @param uri action or link href
 * @param method action or link method
 * @return [Action] the action or link url and method
 */
private fun buildAction(root: String, uri: String, method: String? = "GET"): Action {
    val url = URL(root + uri)
    val httpMethod = HttpMethod.valueOf(
        method ?: throw IllegalArgumentException(HTTP_METHOD_REQUIRED)
    )
    return Action(url, httpMethod)
}

/**
 * Builds a request using an [Action]
 *
 * @param action action to build the request from
 * @param body request body
 * @param userToken token of the user performing the action
 * @return [Request]
 */
fun buildRequest(action: Action, userToken: String? = null, body: String? = null): Request =
    buildRequest(action.url, action.method, body, userToken)

/**
 * Builds a request and sends it using the [client]
 *
 * @param client HTTP client
 * @param jsonFormatter
 * @param action action to build the request from
 * @param userToken token of the user performing the action
 * @return [SirenEntity] response entity
 */
suspend inline fun <reified T> buildAndSendRequest(
    client: OkHttpClient,
    jsonFormatter: Gson,
    action: Action,
    userToken: String? = null,
    body: String? = null
): SirenEntity<T> {
    val request = buildRequest(action, userToken, body)
    Log.v("REQUEST", request.toString())

    return request.send(client) {
        handle(
            SirenEntity.getType<T>().type,
            jsonFormatter
        )
    }
}