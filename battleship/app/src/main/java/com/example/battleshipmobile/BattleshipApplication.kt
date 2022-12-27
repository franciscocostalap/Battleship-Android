package com.example.battleshipmobile

import android.app.Application
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.game.RealGameService
import com.example.battleshipmobile.battleship.service.user.RealUserService
import com.example.battleshipmobile.battleship.service.user.UserService
import com.example.battleshipmobile.battleship.http.ResendCookiesJar
import com.example.battleshipmobile.battleship.http.SharedPrefsCookieStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import java.net.URL


const val TAG = "BattleshipGameApp"

interface DependenciesContainer{
    val userService: UserService
    val statisticsService: RankingServiceI
    val authInfoService: AuthInfoService
    val gameService: GameService
}

class RankingServiceI {

}

private const val host = "https://f29b-217-129-147-107.eu.ngrok.io"
private const val root = "$host/api"
private const val home = "$root/"
private const val userHome = "$root/my"
private const val SIZE_50MB: Long = 50 * 1024 * 1024

class BattleshipApplication : Application(), DependenciesContainer {

    private val cookieStore = SharedPrefsCookieStore(this)

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(ResendCookiesJar(cookieStore))
            .cache(Cache(directory = cacheDir, maxSize = SIZE_50MB))
            .build()
    }

    private val jsonEncoder: Gson by lazy {
        GsonBuilder()
            .create()
    }

    override val userService: UserService by lazy {
        RealUserService(httpClient, jsonEncoder, rootUrl = root, parentUrl = URL(home))
    }

    override val gameService: GameService by lazy {
        RealGameService(httpClient, jsonEncoder, rootUrl = root, parentUrl = URL(userHome))
    }

    override val statisticsService: RankingServiceI by lazy {
        RankingServiceI()
        /*RankingService(httpClient,jsonEncoder, rootUrl = host, parentURL = URL(home))*/
    }

    override val authInfoService: AuthInfoService by lazy {
        AuthInfoService(this, cookieStore)
    }
}