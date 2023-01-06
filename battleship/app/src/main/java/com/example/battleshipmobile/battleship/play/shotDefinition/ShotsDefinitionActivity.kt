package com.example.battleshipmobile.battleship.play.shotDefinition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.views.BackPressHandler
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
    private val viewModel: ShotsDefinitionViewModel by viewModels {
        viewModelInit {
            ShotsDefinitionViewModel(dependencies.gameService, dependencies.authInfoService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val shotsDefinitionRules = viewModel.shotsDefinitionRules
            val turn = viewModel.turn
            if (shotsDefinitionRules == null && turn == null)
                viewModel.initializeGame()


            val boards = viewModel.boards
            if (shotsDefinitionRules != null && boards != null && turn != null) {
                val screenState = ShotsDefinitionScreenState(
                    boards = boards,
                    currentShots = viewModel.currentShots,
                    turn = turn,
                    remainingTime = shotsDefinitionRules.shotsDefinitionTimeout,
                    timerResetToggle = viewModel.timerResetToggle
                )

                val screenHandlers = ShotsDefinitionHandlers(
                    onOpponentBoardSquareClicked = { square ->
                        viewModel.onOpponentBoardSquareClicked(square)
                    },
                    onOwnBoardSquareClicked = { square ->
                        showToast("You can't shoot on your own board!")
                    },
                    onSubmitShotsClick = {
                        viewModel.submitShots()
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
                onBackClicked()
            }
        }
    }
    private fun onBackClicked() {
        HomeActivity.navigate(this)
        finish()
    }
}
