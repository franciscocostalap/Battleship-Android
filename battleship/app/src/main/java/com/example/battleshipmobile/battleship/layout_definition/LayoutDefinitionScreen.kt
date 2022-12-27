package com.example.battleshipmobile.battleship.layout_definition


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.service.game.ShipsInfoDTO
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.GameRules
import com.example.battleshipmobile.battleship.service.model.Square
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.game.BoardView
import com.example.battleshipmobile.ui.views.game.FleetCompositionControls
import com.example.battleshipmobile.ui.views.game.FleetCompositionView
import com.example.battleshipmobile.ui.views.game.ShipData

data class LayoutDefinitionHandlers(
    val onSquareClicked: (Square) -> Unit = {},
    val onShipClicked: (ShipData) -> Unit = {},
    val onRotateClicked: () -> Unit = {},
    val onFleetResetClicked: () -> Unit = {},
    val onSubmit: () -> Unit = {}
)

data class LayoutDefinitionScreenState(
    val board: Board,
    val availableShips: List<ShipData>,
    val selectedShip: ShipData?
)

@Composable
fun LayoutDefinitionScreen(
    state: LayoutDefinitionScreenState,
    handlers: LayoutDefinitionHandlers = LayoutDefinitionHandlers(),
) {
    BattleshipMobileTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.LayoutDefinition.Screen),
            floatingActionButton = {
                FleetCompositionControls(
                    onRotateRequested = handlers.onRotateClicked,
                    onResetRequested = handlers.onFleetResetClicked,
                    isShipSelected = state.selectedShip != null
                )
                Button(onClick = { handlers.onSubmit() }) {
                    Text("Submit")
                }
            },
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Text(
                    text = "Define your layout",
                    style = MaterialTheme.typography.h3
                )
                BoardView(
                    board = state.board,
                    modifier = Modifier
                        .testTag(TestTags.LayoutDefinition.Board)
                        .padding(16.dp),
                    onSquareClicked = handlers.onSquareClicked,
                )
                FleetCompositionView(
                    availableShips = state.availableShips,
                    selectedShip = state.selectedShip,
                    onShipSelected = handlers.onShipClicked
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutDefinitionScreen(
        state = LayoutDefinitionScreenState(
            board = Board(
                side = 10,
                shots = listOf(),
                hits = listOf(),
                shipParts = listOf()
            ),
            availableShips = GameRules.FleetComposition(
                name = "Default",
                composition = mapOf(
                    1 to 1,
                    2 to 1,
                    3 to 1,
                    4 to 1,
                    5 to 1
                )
            ).toList(),
            selectedShip = null
        )
    )
}