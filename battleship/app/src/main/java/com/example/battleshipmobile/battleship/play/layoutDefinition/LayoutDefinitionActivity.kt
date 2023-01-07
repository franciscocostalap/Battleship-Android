package com.example.battleshipmobile.battleship.play.layoutDefinition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.shotDefinition.ShotsDefinitionActivity
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.model.State
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.views.BackPressHandler
import com.example.battleshipmobile.ui.views.LoadingContent
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.utils.viewModelInit

class LayoutDefinitionActivity : ComponentActivity() {

    companion object {

        private const val GAME_ID_EXTRA = "gameID"

        fun navigate(origin: Activity, gameID: ID) {
            with(origin) {
                val intent = Intent(this, LayoutDefinitionActivity::class.java)
                intent.putExtra(GAME_ID_EXTRA, gameID)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: LayoutDefinitionViewModel by viewModels {
        viewModelInit {
            LayoutDefinitionViewModel(dependencies.gameService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val gameState = viewModel.gameCurrentState.collectAsState()
            when(gameState.value?.state){
                State.CANCELLED -> {
                    viewModel.onLeave()
                    HomeActivity.navigate(this)
                    finish()
                }
                State.PLAYING -> {
                    viewModel.onLeave()
                    ShotsDefinitionActivity.navigate(this)
                    finish()
                }
                else -> {}
            }

            val gameRules = viewModel.gameRules
            if (gameRules == null)
                viewModel.getGameRules()
            val board = viewModel.board
            val availableShips = viewModel.availableShips
            val isSubmittingDisabled = viewModel.isSubmittingDisabled
            val isTimedOut = viewModel.isTimedOut
            LoadingContent(isLoading = gameRules == null || board == null || availableShips == null){
                check(gameRules != null && board != null && availableShips != null)
                val screenState = LayoutDefinitionScreenState(
                    board = board,
                    availableShips = availableShips,
                    selectedShip = viewModel.selected,
                    isSubmittingDisabled = isSubmittingDisabled,
                )
                val screenHandlers = LayoutDefinitionHandlers(
                    onShipClicked = { shipData -> viewModel.selected = shipData },
                    onSquareClicked = { square ->
                        viewModel.selected?.let { selectedShipData ->
                            viewModel.placeShip(initialSquare = square, shipData = selectedShipData)
                        }
                    },
                    onRotateClicked = { viewModel.rotateSelected() },
                    onFleetResetClicked = {
                        viewModel.resetState()
                        showToast("Fleet reset.")
                    },
                    onSubmit = {
                        viewModel.submitLayout()
                        showToast("Fleet submitted.")
                    },
                    onTimeout = { viewModel.onTimeout() },
                    onBackClicked = { onBackClicked() }
                )
                LayoutDefinitionScreen(
                    state = screenState,
                    handlers = screenHandlers,
                    timeToDefineLayout = gameRules.layoutDefinitionTime
                )
            }
            BackPressHandler {
                onBackClicked()
            }
            if(isTimedOut) {
                ErrorAlert(
                    title = R.string.timeout_title,
                    message = R.string.timeout_placeships_message,
                    onDismiss = {
                        HomeActivity.navigate(this)
                        finish()
                    }
                )
            }
        }
    }

    private fun onBackClicked() {
        viewModel.onLeave()
        HomeActivity.navigate(this)
        finish()
    }

}
