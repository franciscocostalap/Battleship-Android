package com.example.battleshipmobile.battleship.layout_definition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.utils.viewModelInit

class LayoutDefinitionActivity : ComponentActivity() {

    private val viewModel: LayoutDefinitionViewModel by viewModels {
            viewModelInit {
                LayoutDefinitionViewModel()
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
                        viewModel.placeship(
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
            )

            LayoutDefinitionScreen(
                state = screenState,
                handlers = screenHandlers
            )
        }
    }

}
