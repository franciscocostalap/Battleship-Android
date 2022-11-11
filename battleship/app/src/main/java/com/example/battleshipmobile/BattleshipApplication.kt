package com.example.battleshipmobile

import android.app.Application
import com.example.battleshipmobile.battleship.service.user.RealUserService
import com.example.battleshipmobile.battleship.service.user.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient


const val TAG = "BattleshipGameApp"

interface DependenciesContainer{
    val userService: UserService
}

private const val userURL = "https://1a23-2001-818-e245-8100-389b-35be-5c50-1a09.eu.ngrok.io/api/user"
private const val SIZE_50MB: Long = 50 * 1024 * 1024

class BattleshipApplication: Application(), DependenciesContainer{

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(Cache(directory = cacheDir, maxSize = SIZE_50MB))
            .build()
    }

    private val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .create()
    }


    override val userService: UserService by lazy {
        RealUserService(httpClient, jsonEncoder, userURL)
    }

}