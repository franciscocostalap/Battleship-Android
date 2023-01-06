package com.example.battleshipmobile.battleship.play.layoutDefinition

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.http.hypermedia.Problem
import com.example.battleshipmobile.battleship.service.dto.GameRulesDTO
import com.example.battleshipmobile.battleship.service.dto.GameStateInfoDTO
import com.example.battleshipmobile.battleship.service.dto.ShipDTO
import com.example.battleshipmobile.battleship.service.dto.ShipsInfoDTO
import com.example.battleshipmobile.battleship.service.game.*
import com.example.battleshipmobile.battleship.service.model.*
import com.example.battleshipmobile.battleship.service.model.GameRules.FleetComposition
import com.example.battleshipmobile.battleship.service.model.State.*
import com.example.battleshipmobile.ui.views.game.ShipData
import com.example.battleshipmobile.utils.minutesToMillis
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class LayoutDefinitionViewModel(private val gameService: GameService) : ViewModel() {

    var availableShips: List<ShipData> by mutableStateOf(fakeGameRules().fleetComposition.toList())
    private set
    private var placedShips: List<ShipDTO> by mutableStateOf(emptyList())
    var selected: ShipData? by mutableStateOf(null)
    var isTimedOut: Boolean by mutableStateOf(false)
    var gameRules: GameRulesDTO? by mutableStateOf(null)
    var board: Board? by mutableStateOf(null)

    private val _playingGameState = MutableStateFlow<GameStateInfoDTO?>(null)
    val playingGameState = _playingGameState.asStateFlow()


    fun startLayoutDefinitionPhase() = viewModelScope.launch {
        gameService
            .pollGameStateInfo()
            .collectLatest {
                Log.v("GAME_STATE_INFO", it.toString())
                if(it.state == PLAYING) {
                    _playingGameState.value = it
                }
            }
    }

    private fun fakeGameRules(): GameRules {
        return GameRules(
            boardSide = 10,
            layoutDefinitionTime = minutesToMillis(10),
            playTimeout = minutesToMillis(10),
            shotsPerTurn = 1,
            fleetComposition = FleetComposition(
                name = "Default",
                composition = mapOf(
                    1 to 1,
                    2 to 1,
                    3 to 1,
                    4 to 1,
                    5 to 1,
                )
            )
        )
    }

    fun placeShip(initialSquare: Square, shipData: ShipData) {
        val currentBoard = board ?: throw IllegalStateException("Board is not initialized")
        val newBoard = currentBoard.placeShip(initialSquare, shipData.ship)

        if (newBoard != board) {
            board = newBoard
            selected = null
            availableShips = availableShips.filter { it.id != shipData.id }
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
        val currentGameRules = gameRules ?: throw IllegalStateException("Game rules are null")
        board = Board.empty(currentGameRules.boardSide)
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
        availableShips = fakeGameRules().fleetComposition.toList()
        board = Board.empty(fakeGameRules().boardSide)
        placedShips = emptyList()
    }

    fun submitLayout() {
        viewModelScope.launch {
            val result = async {
                gameService.placeShips(ShipsInfoDTO(placedShips))
            }
            result.await()
        }
    }

    fun onTimeout() {
        viewModelScope.launch {
            val result = async {
                try {
                    gameService.placeShips(ShipsInfoDTO(placedShips))
                } catch (problem: Problem) {
                    if (problem.status == 400) {
                        Log.v(
                            "LayoutDefinitionViewModel",
                            "Layout definition timed out with error 400"
                        )
                    }
                } finally {
                    isTimedOut = true
                }

            }
            result.await()
        }
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

