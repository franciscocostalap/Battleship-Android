package com.example.battleshipmobile.utils

import com.example.battleshipmobile.battleship.http.hypermedia.Problem
import com.example.battleshipmobile.battleship.http.hypermedia.ProblemMediaType
import android.util.Log
import com.example.battleshipmobile.utils.HttpMethod.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.io.IOException
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * HTTP methods
 */
enum class HttpMethod {
    GET, POST, PUT, DELETE, PATCH
}

private val errorHttpRange = 400..599

sealed class ApiException(msg: String) : Exception(msg)

/**
 * Exception throw when an unexpected response was received from the API.
 */
class UnexpectedResponseException(
    response: Response
) : ApiException("Unexpected ${response.code} response from the API.")

/**
 * Exception thrown when a required navigation action could not be found in the api's response.
 */
class UnresolvedActionException(msg: String = "") : ApiException(msg)


fun buildRequest(url: URL, method: HttpMethod = GET, body: String? = null, token: String? = null): Request {
    val emptyBody = "{}"
    val bodyToSend = if(method != GET && body == null) emptyBody else body

    return Request.Builder()
        .url(url)
        .addAuthHeaderIfNotNull(token)
        .method(method.name, bodyToSend?.toRequestBody("application/json".toMediaType()))
        .build()
}


private fun Request.Builder.addAuthHeaderIfNotNull(value: String?): Request.Builder{
    value ?: return this
    return addHeader("Authorization", "Bearer $value")
}

fun <T> Response.handle(bodyType: Type, jsonEncoder: Gson): T {
    val actualContentType = body?.contentType()
    val actualBody = body?.string()
    Log.v("RESPONSE BODY", actualBody!!)

    if (isSuccessful && actualContentType != null && actualContentType == SirenMediaType) {
        try {
            return jsonEncoder.fromJson(actualBody, bodyType)
        } catch (e: JsonSyntaxException) {
            throw UnexpectedResponseException(this)
        }
    } else if (this.code in errorHttpRange && actualContentType != null && actualContentType == ProblemMediaType) {
        throw jsonEncoder.fromJson(actualBody, Problem::class.java)
    } else {
        throw UnexpectedResponseException(this)
    }
}

/**
 * Extension function used to send [this] request using [okHttpClient] and process the
 * received response with the given [handler]. Note that [handler] is called from a
 * OkHttp IO Thread.
 *
 * @receiver the request to be sent
 * @param okHttpClient  the client from where the request is sent
 * @param handler       the handler function, which is called from a IO thread.
 *                      Be mindful of threading issues.
 * @return the result of the response [handler]
 */
suspend fun <T> Request.send(okHttpClient: OkHttpClient, handler: Response.() -> T): T =

    suspendCoroutine { continuation ->
        okHttpClient.newCall(request = this).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    Log.v("RESPONSE STATUS", response.code.toString())
                    Log.v("RESPONSE MESSAGE", response.message)
                    continuation.resume(response.handler())
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        })
    }