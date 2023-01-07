package com.example.battleshipmobile.battleship.play.shotDefinition

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.play.shotDefinition.GameTurn.*
import com.example.battleshipmobile.battleship.service.dto.ShotsDefinitionDTO
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.model.*
import com.example.battleshipmobile.battleship.service.model.State.*
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ShotsDefinitionRules(
    val shotsDefinitionTimeout : Long,
    val shotsPerTurn: Int
)

class GameViewModel(
    private val gameService: GameService,
    private val authService: AuthInfoService
) : ViewModel() {

    var boards: GameBoards? by mutableStateOf(null)
    private set
    var turn: GameTurn? by mutableStateOf(null)
    private set
    var timerResetToggle: Boolean by mutableStateOf(false)
    private set
    var shotsDefinitionRules: ShotsDefinitionRules? = null
    private set
    var isLoading: Boolean by mutableStateOf(false)
    private set
    var winner: GameTurn? by mutableStateOf(null)
    private set
    var isTimedOut: Boolean by mutableStateOf(false)
    private set

    init {
        initializeGame()
    }

    private fun initializeGame() {
        viewModelScope.launch {
            isLoading = true
            val deferredValues = listOf(
                async { gameService.getGameRules() },
                async { gameService.getGameStateInfo() },
                async { gameService.getBoard(MY) },
                async { gameService.getBoard(OPPONENT) }
            )

            val (gameRules, gameState, myBoard, opponentBoard)
                = awaitAll(*deferredValues.toTypedArray())

            gameRules as GameRules
            gameState as GameStateInfo
            myBoard as Board
            opponentBoard as Board
            shotsDefinitionRules = ShotsDefinitionRules(
                shotsDefinitionTimeout = gameRules.playTimeout,
                shotsPerTurn = gameRules.shotsPerTurn
            )
            turn =
                if (authService.uid == gameState.turnID)
                    MY
                else
                    OPPONENT
            boards = GameBoards(myBoard, opponentBoard)
            isLoading = false
        }
    }

    private fun changeTurn() =
        if(turn == MY)
            turn = OPPONENT
        else
            turn = MY

    fun handleShot(square: Square) {
        if(turn != MY) return


        val shotRules = checkNotNull(shotsDefinitionRules){
            "Shots definition rules should be initialized by now."
        }

        val opponentBoard = checkNotNull(boards?.opponentBoard){
            "Opponent board should be initialized by now."
        }
        if(opponentBoard.shots.contains(square)) return
        if(opponentBoard.hits.contains(square)) return

        val shotsObservation = opponentBoard.aimedShots
        val newAimedShots = when{
            shotsObservation.contains(square) -> shotsObservation - square
            shotsObservation.size < shotRules.shotsPerTurn -> shotsObservation + square
            else -> shotsObservation
        }


        val newOpponentBoard = opponentBoard.copy(aimedShots = newAimedShots)
        boards = boards?.copy(opponentBoard = newOpponentBoard)
    }

    fun trySubmitShots() {
        if(turn != MY || boards?.opponentBoard?.aimedShots?.size != shotsDefinitionRules?.shotsPerTurn) return
        val shots = boards?.opponentBoard?.aimedShots ?: return
        viewModelScope.launch {
            gameService.makeShots(ShotsDefinitionDTO(shots))
            val newOpponentBoard = gameService.getBoard(OPPONENT)
            boards = boards?.copy(opponentBoard = newOpponentBoard)
            timerResetToggle = !timerResetToggle
            checkWinner()
            changeTurn()
        }
    }

    fun waitForOpponentPlay() {
        viewModelScope.launch {
            //winner check
            val myBoardFlow = gameService.pollMyBoard()
            myBoardFlow.collectLatest { myBoardState ->
                if(myBoardState != boards?.myBoard){
                    boards = boards?.copy(myBoard = myBoardState)
                    timerResetToggle = !timerResetToggle
                    changeTurn()
                    checkWinner()
                    gameService.cancelPollMyBoard()
                }
            }
        }
    }

    fun checkWinner(){
        viewModelScope.launch {
            val gameInfo = gameService.getGameStateInfo()
            if(gameInfo.state == FINISHED) {
                winner = if(gameInfo.turnID == authService.uid) OPPONENT else MY
            }
        }
    }

    fun onTimeout() {
        isTimedOut = true
        onLeave()
    }

    fun onLeave(){
        viewModelScope.launch {
            if (turn != MY) gameService.cancelPollMyBoard()
        }
    }

}
