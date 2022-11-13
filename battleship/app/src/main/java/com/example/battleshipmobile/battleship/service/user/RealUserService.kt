package com.example.battleshipmobile.battleship.service.user

import android.os.Parcelable
import android.util.Log
import com.example.battleshipmobile.utils.Problem
import com.example.battleshipmobile.utils.SirenEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URL
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@Parcelize
data class AuthInfoDTO(val uid: Int, val token: String): Parcelable
fun AuthInfo.toDTO() = AuthInfoDTO(uid, token)



class RealUserService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val userUrl: String
): UserService {

    val loginUrl = URL("$userUrl/login")
    val registerUrl = URL(userUrl)

    suspend fun getHome(){

    }

    override suspend fun register(user: User): AuthInfo {
        return AuthInfo(1, "")
    }


    override suspend fun login(authenticator: User): AuthInfo {

        val body = jsonFormatter.toJson(authenticator)
        Log.d("REQUEST_BODY", body)

        return request<AuthInfo>(client, loginUrl, body, jsonFormatter)
    }

}


private fun <T> Continuation<T>.handle(response: Response, jsonFormatter: Gson){
    val body = response.body?.string()
    Log.d("RESPONSE_BODY", body.toString())

    if(response.code == 200) {
        val sirenEntity = jsonFormatter.fromJson<SirenEntity<T>>(
            body,
            SirenEntity.getType<AuthInfo>().type
        )
        sirenEntity.properties?.let {
            resume(it)
        } ?: resumeWithException(Exception())
    }else{
        val problem = jsonFormatter.fromJson<Problem>(
            body,
            object: TypeToken<Problem>(){}.type
        )
        resumeWithException(Exception()) // Map Problems to Exception
    }
}

private suspend fun <T> request(client: OkHttpClient, url: URL, body: String, jsonFormatter: Gson): T{
    val request = Request
        .Builder()
        .url(url)
        .post(body.toRequestBody("application/json".toMediaType()))
        .build()

    return suspendCoroutine { continuation ->

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.handle(response, jsonFormatter)
            }
        })

    }
}