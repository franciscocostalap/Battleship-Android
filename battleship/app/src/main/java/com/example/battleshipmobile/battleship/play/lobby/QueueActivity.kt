package com.example.battleshipmobile.battleship.play.lobby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.layoutDefinition.LayoutDefinitionActivity
import com.example.battleshipmobile.battleship.play.lobby.QueueState.FULL
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.LoadingContent
import com.example.battleshipmobile.ui.views.TimedComposable
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.utils.viewModelInit

class QueueActivity : ComponentActivity() {

    companion object {

        const val LOBBY_ID_EXTRA = "lobbyID"
        const val GAME_ID_EXTRA = "gameID"
        private const val LOBBY_WAS_NOT_CREATED = "Lobby was not created"
        private const val GAME_WAS_NOT_CREATED = "Game was not created"
        private const val MUST_BE_AUTHENTICATED = "You must be authenticated"
        private const val CANT_PERFORM_BACK_ACTION = "Game is starting, please wait"
        const val DELAY_TIME = 3000L
        private const val TAG = "QUEUE_ACTIVITY"

        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, QueueActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val authRepo by lazy { dependencies.authInfoService }
    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel by viewModels<QueueViewModel> {
        viewModelInit { QueueViewModel(dependencies.gameService) }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "QueueActivity onCreate")

        setContent {
            BattleshipMobileTheme {
                Log.v(TAG, "QueueActivity setContent")
                require(authRepo.hasAuthInfo()) { MUST_BE_AUTHENTICATED }

                val lobbyInfoResult = viewModel.lobbyInformationResult
                val lobby = viewModel.lobby
                if (lobbyInfoResult != null && lobby == null) {
                    Log.v(TAG, "Getting information from lobby result")
                    lobbyInfoResult.onSuccess { lobbyInfo ->
                        if(lobbyInfo.gameID != null){ //second player path
                            viewModel.startGame(lobbyInfo)
                        }else{
                            viewModel.waitForOpponent(lobbyInfo)
                        }
                    }.onFailure {
                        Log.e(TAG, it.stackTraceToString())
                        ErrorAlert(
                            title = R.string.general_error_title,
                            message = R.string.general_error,
                            buttonText = R.string.ok,
                            onDismiss = {
                                HomeActivity.navigate(this)
                                finish()
                            }
                        )
                    }
                }

                LoadingContent(isLoading = lobby == null){
                    require(lobby != null) { LOBBY_WAS_NOT_CREATED }
                    Log.d(TAG, "Lobby: $lobby")
                    if (lobby.state == FULL) {
                        TimedComposable(
                            timeToWait = DELAY_TIME,
                            onTimeout = {
                                val gameID = lobby.gameID
                                require(gameID != null) { GAME_WAS_NOT_CREATED }
                                LayoutDefinitionActivity.navigate(this, gameID)
                                finish()
                            }
                        ) {
                            QueueScreenContent(lobby.state,
                                onBackClick = {
                                    showToast(CANT_PERFORM_BACK_ACTION)
                                }
                            )
                        }

                    } else {
                        //First player to join
                        val cancelQueue = {
                            viewModel.leaveLobby()
                            HomeActivity.navigate(this)
                            finish()
                        }
                        QueueScreenContent(
                            lobby.state,
                            onQueueCancel = cancelQueue,
                            onBackClick = cancelQueue
                        )
                    }
                }
            }
        }

    }

    /**
     * Queue screen content
     *
     */
    @Composable
    private fun QueueScreenContent(
        lobbyState: QueueState,
        onQueueCancel: () -> Unit = { },
        onBackClick: () -> Unit = { }
    ) {
        QueueScreen(
            queueState = lobbyState,
            onBackClick = onBackClick,
            onCancelClick = onQueueCancel)
    }


}



