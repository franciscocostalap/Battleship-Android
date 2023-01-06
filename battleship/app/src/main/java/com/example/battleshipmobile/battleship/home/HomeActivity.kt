package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.auth.AuthenticationActivity
import com.example.battleshipmobile.battleship.info.InfoActivity
import com.example.battleshipmobile.battleship.play.lobby.QueueActivity
import com.example.battleshipmobile.battleship.ranking.RankingActivity
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit

class HomeActivity : ComponentActivity() {

    companion object {

        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        private const val TAG = "HomeActivity"

    }


    private val homeViewModel by viewModels<HomeViewModel> {
        viewModelInit { HomeViewModel(dependencies.authInfoService) }
    }
    private val dependencies by lazy { application as DependenciesContainer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "HomeActivity onCreate")
        setContent {
            BattleshipMobileTheme {
            //needed to recompose 
            val userId = homeViewModel.uid

                HomeScreen(
                    isLoggedIn = homeViewModel.isLoggedIn(),
                    onLoginRequested = { AuthenticationActivity.navigate(this) },
                    onLogoutRequested = { homeViewModel.logout() },
                    onPlayRequested = { QueueActivity.navigate(this) },
                    onRankingRequested = { RankingActivity.navigate(this) },
                    onCreditsRequested = { InfoActivity.navigate(this) }
                )
            }
        }
    }
}
