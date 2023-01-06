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
import com.example.battleshipmobile.battleship.play.shotDefinition.ShotsDefinitionActivity
import com.example.battleshipmobile.battleship.play.shotDefinition.ShotsDefinitionScreen
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.utils.viewModelInit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LayoutDefinitionActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, LayoutDefinitionActivity::class.java)
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

            if (gameRules != null && board != null) {
                val screenState = LayoutDefinitionScreenState(
                    board = board,
                    availableShips = viewModel.availableShips,
                    selectedShip = viewModel.selected,
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
                    },
                    onSubmit = {
                        viewModel.submitLayout()
                    },
                    onTimeout = {
                        viewModel.onTimeout()
                    }
                )

                LayoutDefinitionScreen(
                    state = screenState,
                    handlers = screenHandlers,
                    timeToDefineLayout = gameRules.layoutDefinitionTimeout
                )
            }
        }
        //TODO else loading

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

}
