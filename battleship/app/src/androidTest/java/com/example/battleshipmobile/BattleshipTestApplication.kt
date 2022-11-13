package com.example.battleshipmobile

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.battleshipmobile.battleship.login.AuthInfoRepository
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.UserService
import io.mockk.coEvery
import io.mockk.mockk

class BattleshipTestApplication: DependenciesContainer, Application() {

    private val testInfo = AuthInfo(0, "testToken")

    override val userService: UserService = object : UserService {


        override suspend fun login(authenticator: User): AuthInfo = testInfo
        override suspend fun register(user: User): AuthInfo = testInfo

    }

    override val authInfoRepository: AuthInfoRepository
        get() = mockk(relaxed = true) {
            coEvery { authInfo } returns testInfo
        }
}

@Suppress("unused")
class BattleshipTestRunner: AndroidJUnitRunner(){
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}