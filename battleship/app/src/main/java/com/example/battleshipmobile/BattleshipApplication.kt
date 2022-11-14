package com.example.battleshipmobile

import android.app.Application
import com.example.battleshipmobile.battleship.login.AuthInfoRepository
import com.example.battleshipmobile.battleship.login.AuthInfoRepositorySharedPrefs
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
import com.example.battleshipmobile.battleship.service.lobby.RealLobbyService
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
    val lobbyService: LobbyService
}

private const val host = "https://dfe5-95-92-100-136.eu.ngrok.io"
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

    override val lobbyService: LobbyService by lazy{
        RealLobbyService(httpClient, jsonEncoder, host, URL(root))
    }

    override val authInfoRepository: AuthInfoRepository
        get() = AuthInfoRepositorySharedPrefs(this)
}