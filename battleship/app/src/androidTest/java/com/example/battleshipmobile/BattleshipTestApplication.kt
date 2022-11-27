package com.example.battleshipmobile

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.battleshipmobile.battleship.auth.AuthInfoRepository
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
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

    override var authInfoRepository: AuthInfoRepository = mockk(relaxed = true) {
            every { authInfo } returns AuthInfo(0, "testToken")
        }

    override val lobbyService: LobbyService = mockk(relaxed = true){
        coEvery { getLobbyInfo(any()) } returns LobbyInformation(0, null)
        coEvery { enqueue(any()) } returns LobbyInformation(0, null)
    }
}

@Suppress("unused")
class BattleshipTestRunner: AndroidJUnitRunner(){
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}