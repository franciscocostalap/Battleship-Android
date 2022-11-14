package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.info.InfoActivity
import com.example.battleshipmobile.battleship.login.LoginActivity
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit

class HomeActivity: ComponentActivity() {

    companion object{

        fun navigate(origin: Activity){
            with(origin){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val authRepo by lazy { dependencies.authInfoRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipMobileTheme {
                HomeScreen(
                    isLoggedIn =  authRepo.authInfo != null,
                    onLoginButtonClick = { LoginActivity.navigate(this) },
                    onLogoutButtonClick = { authRepo.authInfo = null },
                    onPlayButtonClick = {   },
                    onRankingButtonClick = {  },
                    onInfoButtonClick = { InfoActivity.navigate(this) }
                )
            }
        }
    }

}