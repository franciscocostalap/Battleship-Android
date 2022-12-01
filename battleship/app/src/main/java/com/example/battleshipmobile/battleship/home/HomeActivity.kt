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
import com.example.battleshipmobile.battleship.play.QueueActivity
import com.example.battleshipmobile.ui.ErrorAlert
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
    }

    private val homeViewModel by viewModels<HomeViewModel> {
        viewModelInit { HomeViewModel(dependencies.lobbyService) }
    }
    private val dependencies by lazy { application as DependenciesContainer }
    private val authRepo by lazy { dependencies.authInfoRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("HOME_ACTIVITY", "HomeActivity onCreate")

        setContent {
            BattleshipMobileTheme {

                val authInfo =
                    authRepo.authInfo ?: throw IllegalArgumentException() //TODO(mudar exceção)

                val lobbyInfo = homeViewModel.lobbyInformationResult

                // Recomposition triggered by lobby information mutable state
                if(lobbyInfo != null) {
                    Log.v("HOME_ACTIVITY", "Play button was pressed")
                    lobbyInfo.onSuccess {
                        QueueActivity.navigate(this, it.lobbyId, it.gameId)
                        finish()
                    }.onFailure {
                        Log.e("QUEUE_ERROR", it.stackTraceToString())

                        ErrorAlert(
                            title = R.string.general_error_title,
                            message = R.string.general_error,
                            buttonText = R.string.ok,
                            onDismiss = { homeViewModel.setLobbyInfoToNull() }
                        )
                    }
                }

                HomeScreen(
                    isLoggedIn = authRepo.authInfo != null,
                    onLoginButtonClick = { AuthenticationActivity.navigate(this) },
                    onLogoutButtonClick = { authRepo.authInfo = null },
                    onPlayButtonClick = {
                        homeViewModel.enqueue(authInfo.token)
                    },
                    onRankingButtonClick = { },
                    onInfoButtonClick = { InfoActivity.navigate(this) }
                )
            }
        }
    }
}
