package com.example.battleshipmobile

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.ranking.PlayerStatisticsDTO
import com.example.battleshipmobile.battleship.service.ranking.RankingServiceI
import com.example.battleshipmobile.battleship.service.ranking.StatisticsEmbedded
import com.example.battleshipmobile.battleship.service.system_info.SysInfoService
import com.example.battleshipmobile.battleship.service.system_info.SystemInfo
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
            every { uid } returns 3
            coEvery { clearAuthInfo()  } returns Unit
            coEvery { hasAuthInfo() } returns true
        }

    override val statisticsService: RankingServiceI = mockk(relaxed = true){
        coEvery { getStatistics() } returns StatisticsEmbedded(0, listOf(PlayerStatisticsDTO(1,"teste",1,1)))
    }

    override var gameService: GameService = mockk(relaxed = true){
        coEvery { getLobbyInformation() } returns LobbyInformation(0, null)
        coEvery { enqueue() } returns LobbyInformation(0, null)
        coEvery { cancelQueue() }
    }
    override val systemInfoService: SysInfoService
        get() = mockk(relaxed = true){
            coEvery { getSysInfo() } returns SystemInfo(
                authors = listOf(
                    SystemInfo.Author(
                        name = "test",
                        email = "test@email.com",
                        github = "https://www.github.com/test",
                        iselID=48265
                    )
                ),
                version = "1.0.0",
            )
        }
}

@Suppress("unused")
class BattleshipTestRunner: AndroidJUnitRunner(){
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}