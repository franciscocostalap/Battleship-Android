package com.example.battleshipmobile.battleship.play.shotDefinition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.ui.views.LoadingContent
import com.example.battleshipmobile.ui.views.general.Alert
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.views.BackPressHandler
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.utils.viewModelInit

class ShotsDefinitionActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, ShotsDefinitionActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: GameViewModel by viewModels {
        viewModelInit {
            GameViewModel(dependencies.gameService, dependencies.authInfoService)
        }
    }

    fun dismissAlert(){
        HomeActivity.navigate(this)
        viewModel.onLeave()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val winner = viewModel.winner
            if(winner != null){
                Alert(
                    message = if(winner == GameTurn.MY) R.string.game_won else R.string.game_lost,
                    buttonText = R.string.ok,
                    onDismiss = ::dismissAlert
                )
            }
            val shotsDefinitionRules = viewModel.shotsDefinitionRules
            val turn = viewModel.turn
            val boards = viewModel.boards
            val isTimedOut = viewModel.isTimedOut
            val timerResetToggle = viewModel.timerResetToggle
            if(isTimedOut) {
                ErrorAlert(
                    title = R.string.timeout_title,
                    message = R.string.timeout_shots_message,
                    onDismiss = ::dismissAlert
                )
            }
            LoadingContent(isLoading = viewModel.isLoading) {
                // Loading checks
                checkNotNull(boards)
                checkNotNull(shotsDefinitionRules)
                checkNotNull(turn)

                val screenState = ShotsDefinitionScreenState(
                    boards = boards,
                    turn = turn,
                    remainingTime = shotsDefinitionRules.shotsDefinitionTimeout,
                    remainingShots = shotsDefinitionRules.shotsPerTurn - boards.opponentBoard.aimedShots.size,
                    timerResetToggle = timerResetToggle
                )

                if(turn != GameTurn.MY) viewModel.waitForOpponentPlay()

                val screenHandlers = ShotsDefinitionHandlers(
                    onOpponentBoardSquareClicked = { square ->
                        viewModel.handleShot(square)
                    },
                    onOwnBoardSquareClicked = {
                        showToast("You can't shoot on your own board!")
                    },
                    onSubmitShotsClick = {
                        viewModel.trySubmitShots()
                        showToast("Shots fired!")
                    },
                    onTimeout = {
                        viewModel.onTimeout()
                    },
                )

                ShotsDefinitionScreen(
                    state = screenState,
                    handlers = screenHandlers,
                    timeToDefineShot = shotsDefinitionRules.shotsDefinitionTimeout,
                )
            }
            BackPressHandler {
                onGameLeave()
            }
        }
    }
    private fun onGameLeave() {
        viewModel.onLeave()
        HomeActivity.navigate(this)
        finish()
    }
}
