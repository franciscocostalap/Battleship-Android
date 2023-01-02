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
import com.example.battleshipmobile.battleship.play.layout_definition.LayoutDefinitionActivity
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
                require(authRepo.hasAuthInfo() ) { MUST_BE_AUTHENTICATED } //TODO() ter erros especificos?

                //Second player to join
                if(gameID != GAME_ID_DEFAULT_VALUE)
                    viewModel.lobby = Queue(FULL, gameID)

                val lobby = viewModel.lobby
                val lobbyId = lobbyID
                require(lobbyId != LOBBY_ID_DEFAULT_VALUE) { LOBBY_WAS_NOT_CREATED }

                if(lobby.state == FULL){
                    TimedComposable(
                        time = DELAY_TIME,
                        onTimeout = {
                            val gameID = viewModel.lobby.gameID
                            require(gameID != null) { GAME_WAS_NOT_CREATED }
                            LayoutDefinitionActivity.navigate(this, gameID)
                            finish()
                        }
                    ) {
                        QueueScreenContent(lobby.state)
                        BackHandler { showToast(CANT_PERFORM_BACK_ACTION) }
                    }
                } else {
                    //First player to join
                    QueueScreenContent(lobby.state)
                }
                return@BattleshipMobileTheme
            }
        }

        if(gameID == GAME_ID_DEFAULT_VALUE){
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.enterLobby()
                    viewModel.pendingMatch.collectLatest {
                        if(it != null){
                            viewModel.lobby = Queue(FULL, it.gameID)
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
    private fun QueueScreenContent(lobbyState: QueueState) {
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
        viewModel.leaveLobby()
        HomeActivity.navigate(this)
        finish()
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



