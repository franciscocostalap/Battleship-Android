package com.example.battleshipmobile

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.battleshipmobile.battleship.service.ranking.RankingServiceI
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
import com.example.battleshipmobile.battleship.service.ranking.StatisticsEmbedded
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.UserService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

class BattleshipTestApplication: DependenciesContainer, Application() {


    override var userService: UserService = mockk(relaxed = true){
        coEvery { login(any()) } returns AuthInfo(0, "testToken")
        coEvery { register(any()) } returns AuthInfo(0, "testToken")
    }

    override var authInfoService: AuthInfoService = mockk(relaxed = true) {
            every { uid } returns AuthInfo(0, "testToken")
        }

    override val statisticsService: RankingServiceI = object : RankingServiceI {

        override suspend fun getStatistics(embeddedPlayers: Boolean): StatisticsEmbedded {
            TODO("Not yet implemented")
        }
    }

    override var lobbyService: LobbyService = mockk(relaxed = true){
//        coEvery { get(any(), ) } returns LobbyInformation(0, null)
//        coEvery { enqueue() } returns LobbyInformation(0, null)
//        coEvery { cancel(any(), )}
    }
}

@Suppress("unused")
class BattleshipTestRunner: AndroidJUnitRunner(){
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}