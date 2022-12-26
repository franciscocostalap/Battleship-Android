package com.example.battleshipmobile.battleship.play

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
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.QueueState.*
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.TimedComposable
import com.example.battleshipmobile.utils.viewModelInit
import kotlinx.coroutines.delay

class QueueActivity : ComponentActivity() {

    companion object {

        const val LOBBY_ID_EXTRA = "lobbyID"
        const val GAME_ID_EXTRA = "gameID"
        private const val LOBBY_WAS_NOT_CREATED = "Lobby was not created"
        private const val MUST_BE_AUTHENTICATED = "You must be authenticated"
        private const val CANT_PERFORM_BACK_ACTION = "Game is starting, please wait"
        const val DELAY_TIME = 3000L

        fun navigate(origin: Activity, lobbyID: ID, gameID: ID?) {
            with(origin) {
                val intent = Intent(this, QueueActivity::class.java)
                intent.putExtra("lobbyID", lobbyID)
                intent.putExtra("gameID", gameID)
                startActivity(intent)
            }
        }
    }

    private val authRepo by lazy { dependencies.authInfoService }
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

                require(authRepo.hasAuthInfo() ) { MUST_BE_AUTHENTICATED }                                //TODO() MUDAR

                val lobbyId = lobbyID
                require(lobbyId != LOBBY_ID_DEFAULT_VALUE) { LOBBY_WAS_NOT_CREATED }
                val gameId = gameID

                if (gameId != GAME_ID_DEFAULT_VALUE) {
                    TimedComposable(
                        time = DELAY_TIME,
                        onTimeout = {
                            //PlaceShipActivity.navigate(this@QueueActivity, ...)
                            finish()
                        }
                    ) {
                        lobbyState = FULL
                        QueueScreenContent()
                        //Action on back pressed
                        BackHandler { showToast(CANT_PERFORM_BACK_ACTION) }
                    }
                } else {
                    QueueScreenContent()
                    WaitOpponentToEnterLobby(
                        id = lobbyId,
                        viewModel = queueViewModel,
                        lobbyStateOnOpponentJoin = FULL
                    )
                }
                return@BattleshipMobileTheme
            }
        }
    }

    /**
     * Queue screen content
     *
     */
    @Composable
    private fun QueueScreenContent() {
        QueueScreen(
            queueState = lobbyState,
            onBackClick = {
                Log.v("QUEUE_ACTIVITY", "Back button was pressed")
                onQueueCancel()
            },
            onCancelClick = {
                Log.v("QUEUE_ACTIVITY", "Cancel button was pressed")
                onQueueCancel()
            })
    }

    private fun onQueueCancel(){
        queueViewModel.cancelQueue(lobbyID)
        HomeActivity.navigate(this)
        finish()
    }

    /**
     * Launches an effect that waits for the opponent to join the lobby.
     * This effect will not be re-launched if the activity is recreated.
     *
     * @param id the lobby id
     * @param viewModel the queue view model
     * @param lobbyStateOnOpponentJoin the lobby state to be set when the opponent joins the lobby
     */
    @Composable
    fun Activity.WaitOpponentToEnterLobby(
        id: ID,
        viewModel: QueueViewModel,
        lobbyStateOnOpponentJoin: QueueState
    ){
        LaunchedEffect(key1 = true){
            viewModel.waitForFullLobby(
                id,
                onContinuation = {
                    lobbyState = lobbyStateOnOpponentJoin
                    //delay time to match the delay time of the opponent when he joins the lobby
                    delay(DELAY_TIME)
                    //PlaceShipActivity.navigate(this@QueueActivity, ...)
                    finish()
                }
            )
        }
    }

    val LOBBY_ID_DEFAULT_VALUE = -1
    private val GAME_ID_DEFAULT_VALUE = -1

    private val lobbyID: ID
        get() =
            intent.getIntExtra(LOBBY_ID_EXTRA, LOBBY_ID_DEFAULT_VALUE)

    private val gameID: ID
        get() =
            intent.getIntExtra(GAME_ID_EXTRA, GAME_ID_DEFAULT_VALUE)

}



