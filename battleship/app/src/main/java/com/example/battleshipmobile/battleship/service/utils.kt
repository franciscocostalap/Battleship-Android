package com.example.battleshipmobile.battleship.service

import android.util.Log
import com.example.battleshipmobile.battleship.http.HttpMethod
import com.example.battleshipmobile.battleship.service.RelationType.*
import com.example.battleshipmobile.utils.*
import com.example.battleshipmobile.battleship.http.HttpMethod.*
import com.example.battleshipmobile.battleship.http.UnresolvedActionException
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
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
 * @param parentSirenEntity entity to check
 * @param relation relation to check
 * @param rootUrl api base url used for all endpoints
 * @param relationType type of connection to check: action or link
 * @return [Action] the action or link url and method
 */

fun <T> ensureAction(
    parentSirenEntity: SirenEntity<T>,
    relation: String,
    rootUrl: String,
    relationType: RelationType,
    embededInfo: Boolean = false
): Action {
    val query = if(embededInfo) "?embedded=true" else ""

    return if (relationType == ACTION) {
        val sirenAction =
            parentSirenEntity.actions?.find { it.name == relation } ?: throw UnresolvedActionException(
                ACTION_NOT_FOUND + relation
            )

        buildAction(rootUrl, sirenAction.href.toString() + query, sirenAction.method)
    } else {
        val sirenLink =
            parentSirenEntity.links?.find { it.rel.contains(relation) }
                ?: throw UnresolvedActionException(LINK_NOT_FOUND + relation)
        buildAction(rootUrl, sirenLink.href.toString() + query)
    }
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
 * @param query query parameters
 * @return [Request]
 */
fun buildRequest(action: Action,  body: String? = null, query : Map<String,String>? = null): Request =
    com.example.battleshipmobile.battleship.http.buildRequest(
        action.url,
        action.method,
        body,
        query
    )

/**
 * Builds a request and sends it using the [client]
 *
 * @param client HTTP client
 * @param jsonFormatter
 * @param action action to build the request from
 * @return [SirenEntity] response entity
 */
suspend inline fun <reified T> buildAndSendRequest(
    client: OkHttpClient,
    jsonFormatter: Gson,
    action: Action,
    body: String? = null,
    query : Map<String, String>? = null
): SirenEntity<T> {
    val request = buildRequest(action, body,query)
    Log.v("APP REQUEST", request.toString())

    return request.send(client) {
        handle(
            SirenEntity.getType<T>().type,
            jsonFormatter
        )
    }
}