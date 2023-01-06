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

enum class RelationType() {
    ACTION,
    LINK
}

data class Relation(val url: URL, val method: HttpMethod = GET)

private const val HTTP_METHOD_REQUIRED = "HTTP method required"
private fun relationNotFound(type: RelationType) =
    "Entity has no ${type.name.lowercase()} with the rel: "

/**
 * Ensures that the action or link is present in the entity
 *
 * @param parentSirenEntity entity to check
 * @param relation relation to check
 * @param rootUrl api base url used for all endpoints
 * @param relationType type of connection to check: action or link
 * @return [Relation] the action or link url and method
 */

fun <T> ensureRelation(
    parentSirenEntity: SirenEntity<T>,
    relation: String,
    rootUrl: String,
    relationType: RelationType,
    embeddedInfo: Boolean = false
): Relation {
    val query = if (embeddedInfo) "?embedded=true" else ""

    return if (relationType == ACTION) {
        val sirenAction =
            parentSirenEntity.actions?.find { it.name == relation }
                ?: throw UnresolvedActionException(relationNotFound(ACTION) + relation)

        buildRelation(rootUrl, sirenAction.href.toString() + query, sirenAction.method)
    } else {
        val sirenLink =
            parentSirenEntity.links?.find { it.rel.contains(relation) }
                ?: throw UnresolvedActionException(relationNotFound(LINK) + relation)

        buildRelation(rootUrl, sirenLink.href.toString() + query)
    }
}

/**
 * Builds an [Relation]
 *
 * @param root api base url used for all endpoints
 * @param uri action or link href
 * @param method action or link method
 * @return [Relation] the action or link url and method
 */
private fun buildRelation(root: String, uri: String, method: String? = "GET"): Relation {
    val url = URL(root + uri)
    val httpMethod = HttpMethod.valueOf(
        method ?: throw IllegalArgumentException(HTTP_METHOD_REQUIRED)
    )
    return Relation(url, httpMethod)
}

/**
 * Builds a request using an [Relation]
 *
 * @param relation action to build the request from
 * @param body request body
 * @param query query parameters
 * @return [Request]
 */
fun buildRequest(
    relation: Relation,
    body: String? = null,
    query: Map<String, String>? = null
): Request =
    com.example.battleshipmobile.battleship.http.buildRequest(
        relation.url,
        relation.method,
        body,
        query
    )

/**
 * Builds a request and sends it using the [client]
 *
 * @param client HTTP client
 * @param jsonFormatter
 * @param relation action to build the request from
 * @return [SirenEntity] response entity
 */
suspend inline fun <reified T> buildAndSendRequest(
    client: OkHttpClient,
    jsonFormatter: Gson,
    relation: Relation,
    body: String? = null,
    query: Map<String, String>? = null
): SirenEntity<T> {
    val request = buildRequest(relation, body, query)
    Log.v("APP REQUEST", request.toString())

    return request.send(client) {
        handle(
            SirenEntity.getType<T>().type,
            jsonFormatter
        )
    }
}

/**
 * Extracts the strings between the : and / in the template
 * Could be one or multiple strings
 * Example: the given URI: /api/games/1 and the template: /api/games/:gameID
 * will return the object {gameID: "1"}
 *
 * @param uri The URI to extract the values from.
 * @param template The template to extract the values from.
 * @returns An object with the extracted values.
 */
fun extractValues(uri: String, template: String): Map<String, String> {
    val uriParts = uri.split("/")
    val templateParts = template.split("/")

    val values = mutableMapOf<String, String>()

    for (i in uriParts.indices) {
        if (templateParts[i].startsWith(":")) {
            values[templateParts[i].substring(1)] = uriParts[i]
        }
    }

    return values
}

fun SirenEntity<*>.filterEmbeddedEntitiesFor(rel: String) =
    this.entities
        ?.filterIsInstance<EmbeddedEntity<*>>()
        ?.filter { (it.rel[0] == rel) }
        ?: throw IllegalStateException("No embedded entities in response")


fun SirenEntity<*>.filterEmbeddedLinksFor(rel: String) =
    this.links
        ?.filterIsInstance<EmbeddedLink>()
        ?.filter { (it.rel[0] == rel) }
        ?: throw IllegalStateException("No embedded links in response")