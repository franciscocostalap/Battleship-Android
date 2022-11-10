package com.example.battleshipmobile

import android.app.Application
import com.example.battleshipmobile.service.RealUserService
import com.example.battleshipmobile.service.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient


const val TAG = "BattleshipGameApp"

interface DependenciesContainer{
    val userService: UserService
}

private const val userURL = "https://9ce5-194-210-198-137.eu.ngrok.io/api/user"
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