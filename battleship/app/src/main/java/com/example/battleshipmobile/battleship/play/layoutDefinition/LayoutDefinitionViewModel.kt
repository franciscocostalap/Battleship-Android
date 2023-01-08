package com.example.battleshipmobile.battleship.play.layoutDefinition

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.dto.*
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.model.*
import com.example.battleshipmobile.battleship.service.model.GameRules.FleetComposition
import com.example.battleshipmobile.ui.views.game.ShipData
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LayoutDefinitionViewModel(private val gameService: GameService) : ViewModel() {

    var availableShips: List<ShipData>? by mutableStateOf(null)
    private set

    private var placedShips: List<ShipDTO> by mutableStateOf(emptyList())
    var selected: ShipData? by mutableStateOf(null)
    var isTimedOut: Boolean by mutableStateOf(false)
    private set
    var gameRules: GameRules? by mutableStateOf(null)
    var board: Board? by mutableStateOf(null)
    var isSubmittingDisabled : Boolean by mutableStateOf(false)
    private set

    private val _playingGameState = MutableStateFlow<GameStateInfo?>(null)
    val gameCurrentState = _playingGameState.asStateFlow()


    private fun waitForOpponent() {
        viewModelScope.launch {
            gameService
                .pollGameStateInfo()
                .collectLatest {
                    Log.v("GAME_STATE_INFO", it.toString())
                    _playingGameState.value = it
                    if(it.state == State.PLAYING || it.state == State.CANCELLED) {
                        onLeave()
                    }
                }
            }
    }

    fun placeShip(initialSquare: Square, shipData: ShipData) {
        val currentBoard = board ?: throw IllegalStateException("Board is not initialized")
        val newBoard = currentBoard.placeShip(initialSquare, shipData.ship)

        if (newBoard != board) {
            board = newBoard
            selected = null
            val ships = requireNotNull(availableShips)
            availableShips = ships.filter { it.id != shipData.id }
            placedShips = placedShips + ShipDTO(
                initialSquare,
                shipData.ship.size,
                shipData.ship.orientation
            )
        }
    }

    fun getGameRules() = viewModelScope.launch {
        val result = async {
            gameService.getGameRules()
        }
        gameRules = result.await()
        resetAvailableShips()
        clearBoard()
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

    fun resetState() {
        clearBoard()
        resetAvailableShips()
        placedShips = emptyList()
    }

    fun submitLayout() {
        viewModelScope.launch {
            try {
                gameService.placeShips(ShipsInfoDTO(placedShips))
                waitForOpponent()
            }catch (e: Throwable) {
                if(e is CancellationException) throw e
                Log.e("SUBMIT_LAYOUT", e.message ?: "Unknown error")
                throw e
            }
        }.invokeOnCompletion {
            if(it != null) isSubmittingDisabled = false
        }
        isSubmittingDisabled = true
    }

    fun onTimeout() {
        isTimedOut = true
        onLeave()
    }
    fun onLeave(){
        viewModelScope.launch {
            gameService.cancelPollingGameState()
        }
    }

    private fun clearBoard() {
        val currentGameRules = gameRules ?: throw IllegalStateException("Game rules are null")
        board = Board.empty(currentGameRules.boardSide)
    }

    private fun resetAvailableShips() {
        val currentGameRules = gameRules ?: throw IllegalStateException("Game rules are null")
        availableShips = currentGameRules.fleetComposition.toList()
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

