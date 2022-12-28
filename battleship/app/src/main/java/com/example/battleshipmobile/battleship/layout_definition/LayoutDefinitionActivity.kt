package com.example.battleshipmobile.battleship.layout_definition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.utils.viewModelInit

class LayoutDefinitionActivity : ComponentActivity() {

    companion object {

        const val GAME_ID_EXTRA = "gameID"

        fun navigate(origin: Activity,  gameID: ID) {
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

            val screenState = LayoutDefinitionScreenState(
                board = viewModel.board,
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
                }
            )

            LayoutDefinitionScreen(
                state = screenState,
                handlers = screenHandlers
            )
        }
    }

}
