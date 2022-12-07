package com.example.battleshipmobile.battleship.layout_definition

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.battleshipmobile.battleship.service.model.*
import com.example.battleshipmobile.battleship.service.model.GameRules.FleetComposition
import com.example.battleshipmobile.ui.views.game.ShipData
import com.example.battleshipmobile.utils.minutesToMillis

class LayoutDefinitionViewModel: ViewModel() {

    var availableShips: List<ShipData> by mutableStateOf(gameRules().fleetComposition.toList())
    private set
    var selected: ShipData? by mutableStateOf(null)
    var board: Board by mutableStateOf(Board.empty(gameRules().boardSide))

    private fun gameRules(): GameRules{
        return GameRules(
            boardSide = 10,
            layoutDefinitionTime = minutesToMillis(10),
            playTimeout = minutesToMillis(10),
            shotsPerTurn = 1,
            fleetComposition = FleetComposition(
                name = "Default",
                composition = mapOf(
                    1 to 1,
                    2 to 2,
                    3 to 1,
                    4 to 1,
                    5 to 1,
                )
            )
        )
    }

    fun placeship(initialSquare: Square, shipData: ShipData) {
        val newBoard = board.placeShip(initialSquare, shipData.ship)

        if (newBoard != board) {
            board = newBoard
            selected = null
            availableShips = availableShips.filter { it.id != shipData.id }
        }
    }

    fun rotateSelected() {
        selected?.let { shipData ->
            selected = shipData.copy(
                ship = shipData.ship.copy(
                    orientation = shipData.ship.orientation.opposite()
                )
            )
        }
    }

    fun resetState(){
        availableShips = gameRules().fleetComposition.toList()
        board = Board.empty(gameRules().boardSide)
    }

}

fun FleetComposition.toList(): List<ShipData> {
    return composition.flatMap { (shipSize, shipCount) ->
        (0 until shipCount).map { index ->
            ShipData(
                id = shipSize.toString() + index.toString(),
                ship = Ship(size = shipSize, orientation = Orientation.Horizontal)
            )
        }
    }
}