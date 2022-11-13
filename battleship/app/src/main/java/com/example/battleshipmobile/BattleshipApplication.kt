package com.example.battleshipmobile

import android.app.Application
import com.example.battleshipmobile.battleship.login.AuthInfoRepository
import com.example.battleshipmobile.battleship.login.AuthInfoRepositorySharedPrefs
import com.example.battleshipmobile.battleship.service.user.RealUserService
import com.example.battleshipmobile.battleship.service.user.UserService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.net.URL


const val TAG = "BattleshipGameApp"

interface DependenciesContainer{
    val userService: UserService
    val authInfoRepository: AuthInfoRepository
}

private const val host = "http://10.0.2.2:8080"
private const val root = "$host/api/"
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
        RealUserService(httpClient, jsonEncoder, host, URL(root))
    }

    override val authInfoRepository: AuthInfoRepository
        get() = AuthInfoRepositorySharedPrefs(this)
}