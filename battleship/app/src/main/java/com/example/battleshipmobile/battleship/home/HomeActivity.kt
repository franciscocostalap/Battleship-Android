package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.info.InfoActivity
import com.example.battleshipmobile.battleship.auth.AuthenticationActivity
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

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
                    onLoginRequested = {
                        AuthenticationActivity.navigate(this)
                        finish()
                    },
                    onLogoutRequested = { authRepo.authInfo = null },
                    onPlayRequested = {   },
                    onRankingRequested = {  },
                    onInfoRequested = { InfoActivity.navigate(this) }
                )
            }
        }
    }

}