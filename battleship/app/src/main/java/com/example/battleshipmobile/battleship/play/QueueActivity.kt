package com.example.battleshipmobile.battleship.play

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.QueueState.*
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit


class QueueActivity : ComponentActivity() {

    companion object {

        private const val LOBBY_ID_EXTRA = "lobbyID"

        fun navigate(origin: Activity, lobbyID: ID) {
            with(origin) {
                val intent = Intent(this, QueueActivity::class.java)
                intent.putExtra("lobbyID", lobbyID)
                startActivity(intent)
            }
        }
    }


    private val authRepo by lazy { dependencies.authInfoRepository }
    private val dependencies by lazy { application as DependenciesContainer }
    private val queueViewModel by viewModels<QueueViewModel> {
        viewModelInit { QueueViewModel(dependencies.lobbyService) }
    }
    private var lobbyState by mutableStateOf(SEARCHING_OPPONENT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("QUEUE_ACTIVITY", "QueueActivity onCreate")

        setContent {
            BattleshipMobileTheme {
                Log.v("LOBBY_INFO", "Lobby was created")
                val authInfo = authRepo.authInfo
                require(authInfo != null) { "User must be authenticated" } //TODO() MUDAR

                QueueScreen(lobbyState, onCancelClick = {
                    queueViewModel.cancelQueue(lobbyID, authInfo.token)
                    HomeActivity.navigate(this)
                    finish()
                })
                val lobbyId = lobbyID

                if (lobbyId != LOBBY_ID_DEFAULT_VALUE) {
                    Log.v("QUEUE_FLOW", "Waiting for the opponent to join")
                    queueViewModel.waitForFullLobby(
                        lobbyID,
                        authInfo.token,
                        onContinuation = {
                            lobbyState = FULL
                            //PlaceShipActivity.navigate(this@QueueActivity, ...)
                            //finish()
                        }
                    )
                }
                return@BattleshipMobileTheme
            }
        }
    }

    private val LOBBY_ID_DEFAULT_VALUE = -1
    private val lobbyID: ID
        get() =
            intent.getIntExtra(LOBBY_ID_EXTRA, LOBBY_ID_DEFAULT_VALUE)

}