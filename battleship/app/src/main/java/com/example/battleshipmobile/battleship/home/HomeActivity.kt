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
import com.example.battleshipmobile.battleship.info.InfoActivity
import com.example.battleshipmobile.battleship.login.LoginActivity
import com.example.battleshipmobile.battleship.play.QueueActivity
import com.example.battleshipmobile.battleship.play.QueueScreen
import com.example.battleshipmobile.battleship.play.QueueState.FULL
import com.example.battleshipmobile.ui.ErrorAlert
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

    private val homeViewModel by viewModels<HomeViewModel>{
        viewModelInit { HomeViewModel(dependencies.lobbyService) }
    }
    private val dependencies by lazy { application as DependenciesContainer }
    private val authRepo by lazy { dependencies.authInfoRepository }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipMobileTheme {
                val authInfo = authRepo.authInfo ?: throw IllegalArgumentException() //TODO(mudar exceção)

                homeViewModel.lobbyInformation?.onSuccess { lobbyInfo ->
                    if(lobbyInfo.gameId == null){
                        QueueActivity.navigate(this, lobbyInfo.lobbyId)
                        return@BattleshipMobileTheme
                    }

                    Log.v("LOBBY_INFO", "Lobby is full")
                    QueueScreen(queueState = FULL)
                    return@BattleshipMobileTheme
                    //PlaceShipActivity
                }?.onFailure {
                    Log.e("QUEUE_ERROR", it.stackTraceToString())

                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = { homeViewModel.setLobbyInfoToNull() }
                    )
                }

                HomeScreen(
                    isLoggedIn =  authRepo.authInfo != null,
                    onLoginButtonClick = { LoginActivity.navigate(this) },
                    onLogoutButtonClick = { authRepo.authInfo = null },
                    onPlayButtonClick = {
                        homeViewModel.enqueue(authInfo.token)
                    },
                    onRankingButtonClick = {  },
                    onInfoButtonClick = { InfoActivity.navigate(this) }
                )
            }
        }
    }

}