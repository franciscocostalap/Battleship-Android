package com.example.battleshipmobile.battleship.play.layoutDefinition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.battleship.play.lobby.QueueActivity
import com.example.battleshipmobile.battleship.play.shotDefinition.ShotsDefinitionActivity
import com.example.battleshipmobile.battleship.play.shotDefinition.ShotsDefinitionScreen
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.views.BackPressHandler
import com.example.battleshipmobile.ui.views.LoadingScreen
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.utils.viewModelInit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LayoutDefinitionActivity : ComponentActivity() {

    companion object {

        const val GAME_ID_EXTRA = "gameID"

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

            val gameRules = viewModel.gameRules
            if (gameRules == null)
                viewModel.getGameRules()
            val board = viewModel.board
            val availableShips = viewModel.availableShips
            val isSubmittingDisabled = viewModel.isSubmittingDisabled
            val isTimedOut = viewModel.isTimedOut

            if (gameRules != null && board != null && availableShips != null) {
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
                            viewModel.placeShip(
                                initialSquare = square,
                                shipData = selectedShipData
                            )
                        }
                    },
                    onRotateClicked = {
                        viewModel.rotateSelected()
                    },
                    onFleetResetClicked = {
                        viewModel.resetState()
                        showToast("Fleet reset.")
                    },
                    onSubmit = {
                        viewModel.submitLayout()
                    },
                    onTimeout = {
                        viewModel.onTimeout()
                    },
                    onBackClicked = {
                        onBackClicked()
                    }
                )
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

                LayoutDefinitionScreen(
                    state = screenState,
                    handlers = screenHandlers,
                    timeToDefineLayout = gameRules.layoutDefinitionTimeout
                )
            }else{
                LoadingScreen()
            }
            BackPressHandler {
                onBackClicked()
            }

        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.startLayoutDefinitionPhase()
                viewModel.playingGameState.collectLatest {
                    if (it != null) {
                        Log.v("LAYOUT_DEFINITION", "State changed to ${it.state}")
                        ShotsDefinitionActivity.navigate(this@LayoutDefinitionActivity)
                    }
                }
            }
        }
    }
    private fun onBackClicked() {
        HomeActivity.navigate(this)
        finish()
    }

}
