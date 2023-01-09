package com.example.battleshipmobile.battleship.play.lobby

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.layoutDefinition.LayoutDefinitionActivity
import com.example.battleshipmobile.battleship.play.lobby.QueueState.*
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.LoadingContent
import com.example.battleshipmobile.ui.views.LoadingScreen
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

                if(lobbyInfoResult == null){
                    Log.v(TAG, "Entering lobby")
                    viewModel.enterLobby()
                }

                Log.v(TAG, "isPooling: $viewModel.isPooling")
                val queueInfo = viewModel.lobby

                if ( queueInfo != null && queueInfo.gameID == null && !viewModel.isPooling ){
                    viewModel.isPooling = true
                    Log.v (TAG, "Pooling started")
                    viewModel.startPollingLobby()
                }

                if (lobbyInfoResult != null && queueInfo == null) {
                    Log.v(TAG, "Getting information from lobby result")
                    lobbyInfoResult.onSuccess { lobbyInfo ->
                        if(lobbyInfo.gameID != null){ //second player path
                            viewModel.lobby = Queue(
                                state = FULL,
                                lobbyID = lobbyInfo.lobbyID,
                                gameID = lobbyInfo.gameID
                            )
                        }else{
                            viewModel.lobby = Queue(
                                state = SEARCHING_OPPONENT,
                                lobbyID = lobbyInfo.lobbyID,
                            )
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


                LoadingContent(isLoading = queueInfo == null){
                    require(queueInfo != null) { LOBBY_WAS_NOT_CREATED }
                    Log.d(TAG, "Lobby: $queueInfo")

                    if (queueInfo.state == FULL) {

                        TimedComposable(
                            timeToWait = DELAY_TIME,
                            onTimeout = {
                                val gameID = queueInfo.gameID
                                require(gameID != null) { GAME_WAS_NOT_CREATED }
                                LayoutDefinitionActivity.navigate(this, gameID)
                                finish()
                            }
                        ) {
                            QueueScreenContent(
                                queueInfo.state,
                                onQueueCancel = {
                                    viewModel.leaveLobby()
                                    HomeActivity.navigate(this)
                                    finish()
                                }
                            )

                            BackHandler { showToast(CANT_PERFORM_BACK_ACTION) }
                        }

                    } else {
                        //First player to join
                        QueueScreenContent(
                            queueInfo.state,
                            onQueueCancel = {
                                viewModel.leaveLobby()
                                HomeActivity.navigate(this)
                                finish()
                            }
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
    private fun QueueScreenContent(lobbyState: QueueState, onQueueCancel: () -> Unit = { }) {

        QueueScreen(
            queueState = lobbyState,
            onBackClick = {
                Log.v(TAG, "Back button was pressed")
                onQueueCancel()
            },
            onCancelClick = {
                Log.v(TAG, "Cancel button was pressed")
                onQueueCancel()
            })
    }


}



