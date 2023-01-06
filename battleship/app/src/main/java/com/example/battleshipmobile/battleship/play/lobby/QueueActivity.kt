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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.QueueScreen
import com.example.battleshipmobile.battleship.play.layoutDefinition.LayoutDefinitionActivity
import com.example.battleshipmobile.battleship.play.lobby.QueueState.*
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.TimedComposable
import com.example.battleshipmobile.utils.viewModelInit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QueueActivity : ComponentActivity() {

    companion object {

        const val LOBBY_ID_EXTRA = "lobbyID"
        const val GAME_ID_EXTRA = "gameID"
        private const val LOBBY_WAS_NOT_CREATED = "Lobby was not created"
        private const val GAME_WAS_NOT_CREATED = "Game was not created"
        private const val MUST_BE_AUTHENTICATED = "You must be authenticated"
        private const val CANT_PERFORM_BACK_ACTION = "Game is starting, please wait"
        const val DELAY_TIME = 3000L

        fun navigate(origin: Activity, lobbyID: ID, gameID: ID?) {
            with(origin) {
                val intent = Intent(this, QueueActivity::class.java)
                intent.putExtra(LOBBY_ID_EXTRA, lobbyID)
                intent.putExtra(GAME_ID_EXTRA, gameID)
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
        Log.v("QUEUE_ACTIVITY", "QueueActivity onCreate")

        setContent {
            BattleshipMobileTheme {
                Log.v("QUEUE_ACTIVITY", "QueueActivity setContent")
                check(authRepo.hasAuthInfo()) { MUST_BE_AUTHENTICATED } //TODO() ter erros especificos?

                //Second player to join
                val gameID = gameIDExtra
                if (gameID != null)
                    viewModel.startGame(gameID)

                val lobby = viewModel.lobby
                val lobbyId = lobbyIDExtra
                require(lobbyId != NO_LOBBY_EXTRA) { LOBBY_WAS_NOT_CREATED }

                if (lobby.state == FULL) {
                    TimedComposable(
                        time = DELAY_TIME,
                        onTimeout = {
                            val currentGameID = viewModel.lobby.gameID
                            require(currentGameID != null) { GAME_WAS_NOT_CREATED }
                            LayoutDefinitionActivity.navigate(this, currentGameID)
                            finish()
                        }
                    ) {
                        QueueScreenContent(lobby.state)
                        BackHandler { showToast(CANT_PERFORM_BACK_ACTION) }
                    }
                } else {
                    //First player to join
                    QueueScreenContent(
                        lobbyState = lobby.state,
                        onQueueCancel = {
                            viewModel.leaveLobby()
                            HomeActivity.navigate(this)
                            finish()
                        }
                    )
                }
                return@BattleshipMobileTheme
            }
        }

        if (gameIDExtra == null) {
            // First player to join needs to create the lobby and wait for the second player
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.enterLobby()
                    viewModel.pendingMatch.collectLatest { lobbyInfo ->
                        val receivedGameID = lobbyInfo?.gameID
                        receivedGameID?.let {
                            viewModel.startGame(it)
                        }
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
                Log.v("QUEUE_ACTIVITY", "Back button was pressed")
                onQueueCancel()
            },
            onCancelClick = {
                Log.v("QUEUE_ACTIVITY", "Cancel button was pressed")
                onQueueCancel()
            })
    }


    private val NO_GAME_EXTRA = -1
    private val NO_LOBBY_EXTRA = -1

    private val lobbyIDExtra: ID?
        get() = intent
            .getIntExtra(LOBBY_ID_EXTRA, NO_LOBBY_EXTRA)
            .takeIf { it != NO_LOBBY_EXTRA }

    private val gameIDExtra: ID?
        get() = intent
            .getIntExtra(GAME_ID_EXTRA, NO_GAME_EXTRA)
            .takeIf { it != NO_GAME_EXTRA }

}



