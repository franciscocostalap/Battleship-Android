package com.example.battleshipmobile.battleship.play.shotDefinition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.ui.views.LoadingContent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val shotsDefinitionRules = viewModel.shotsDefinitionRules
            val turn = viewModel.turn
            val boards = viewModel.boards

            LoadingContent(isLoading = viewModel.isLoading) {
                // Loading checks
                checkNotNull(boards)
                checkNotNull(shotsDefinitionRules)
                checkNotNull(turn)

                val screenState = ShotsDefinitionScreenState(
                    boards = boards,
                    turn = turn,
                    remainingTime = shotsDefinitionRules.shotsDefinitionTimeout,
                    timerResetToggle = viewModel.timerResetToggle
                )

                if(turn != GameTurn.MY) viewModel.waitForOpponentPlay()

                val screenHandlers = ShotsDefinitionHandlers(
                    onOpponentBoardSquareClicked = { square ->
                        viewModel.handleShot(square)
                    },
                    onSubmitShotsClick = {
                        viewModel.trySubmitShots()
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
        }
    }

}
