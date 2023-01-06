package com.example.battleshipmobile.battleship.play.shotDefinition

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.play.shotDefinition.GameTurn.*
import com.example.battleshipmobile.battleship.service.dto.GameRulesDTO
import com.example.battleshipmobile.battleship.service.dto.GameStateInfoDTO
import com.example.battleshipmobile.battleship.service.dto.ShotsDefinitionDTO
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.Square
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

data class ShotsDefinitionRules(
    val shotsDefinitionTimeout : Long,
    val shotsPerTurn: Int
)

class ShotsDefinitionViewModel(
    private val gameService: GameService,
    private val authService: AuthInfoService
) : ViewModel() {

    var boards: GameBoards? by mutableStateOf(null)
    private set
    var currentShots: List<Square> by mutableStateOf(emptyList())
    var turn: GameTurn? by mutableStateOf(null)
    var timerResetToggle: Boolean by mutableStateOf(false)
    var shotsDefinitionRules: ShotsDefinitionRules? by mutableStateOf(null)


    fun initializeGame() {
        viewModelScope.launch {
            val gameStateDTO = async { gameService.getGameStateInfo() }
            val gameRulesDTO = async { gameService.getGameRules() }
            val myBoardDTO = async { gameService.getBoard(MY) }
            val opponentBoardDTO = async { gameService.getBoard(OPPONENT) }

            val (
                gameState,
                gameRules,
                myBoard,
                opponentBoard
            ) = awaitAll(gameStateDTO, gameRulesDTO, myBoardDTO, opponentBoardDTO)

            gameRules as GameRulesDTO
            gameState as GameStateInfoDTO
            myBoard as Board
            opponentBoard as Board

            shotsDefinitionRules = ShotsDefinitionRules(
                shotsDefinitionTimeout = gameRules.playTimeout,
                shotsPerTurn = gameRules.shotsPerTurn
            )

            turn =
                if (authService.uid == gameState.player1ID)
                    MY
                else
                    OPPONENT

            boards = GameBoards(myBoard,opponentBoard)
        }
    }

    private fun changeTurn() =
        if(turn == MY)
            turn = OPPONENT
        else
            turn = MY

    fun onOpponentBoardSquareClicked(square: Square) {
        if(turn != MY) return
        val shotRules = shotsDefinitionRules ?: throw IllegalStateException("Shots definition rules are not initialized")

        currentShots = if(currentShots.contains(square))
            currentShots.filter { it != square }
        else if(currentShots.size < shotRules.shotsPerTurn)
            currentShots + square
        else
            currentShots
    }

    fun submitShots() {
        if(turn != MY) return

        viewModelScope.launch {
            val newOpponentBoard = gameService.makeShots(ShotsDefinitionDTO(currentShots))
            //boards = boards?.copy(opponentBoard = newOpponentBoard) //TODO board dto e board sao iguais por tudo o mesmo tipo
            changeTurn()
            currentShots = emptyList()
        }
    }

    fun onTimeout(){
        TODO()
    }

}
