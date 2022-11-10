package com.example.battleshipmobile.service

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.versionedparcelable.VersionedParcelize
import com.example.battleshipmobile.utils.Problem
import com.example.battleshipmobile.utils.SirenEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface UserService{

    /**
     *
     */
    suspend fun register(user: User): ServiceResult<AuthInfo?>

    /**
     *
     */
    suspend fun login(authenticator: User): ServiceResult<AuthInfo?>

}

data class User(val username: String, val password: String)
data class AuthInfo(val uid: Int, val token: String)
fun AuthInfo.toDTO() = AuthInfoDTO(uid, token)
data class AuthInfoDTO(val uid: Int, val token: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AuthInfoDTO> {
        override fun createFromParcel(parcel: Parcel): AuthInfoDTO {
            return AuthInfoDTO(parcel)
        }

        override fun newArray(size: Int): Array<AuthInfoDTO?> {
            return arrayOfNulls(size)
        }
    }
}

class RealUserService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val userUrl: String
): UserService{

    val loginUrl = URL("$userUrl/login")
    val registerUrl = URL(userUrl)

    override suspend fun register(user: User): ServiceResult<AuthInfo?> {
        return ServiceResult.error(Problem())
    }


    override suspend fun login(authenticator: User): ServiceResult<AuthInfo?> {

        val body = jsonFormatter.toJson(authenticator)


        Log.d("REQUEST_BODY", body)

        val request = Request
            .Builder()
            .url(loginUrl)
            .post(body.toRequestBody("application/json".toMediaType()))
            .build()

        return suspendCoroutine { continuation ->

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {


                    val body = response.body?.string()
                    Log.d("RESPONSE_BODY", body.toString())
                    if(response.code == 200){
                        val sirenEntity = jsonFormatter.fromJson<SirenEntity<AuthInfo>>(
                            body,
                            SirenEntity.getType<AuthInfo>().type
                        )

                        continuation.resume(ServiceResult.success(sirenEntity.properties))
                    }else{

                        val problem = jsonFormatter.fromJson<Problem>(
                            body,
                            object: TypeToken<Problem>(){}.type
                        )

                        continuation.resume(ServiceResult.error(problem))
                    }
                }
            })

        }

    }

}