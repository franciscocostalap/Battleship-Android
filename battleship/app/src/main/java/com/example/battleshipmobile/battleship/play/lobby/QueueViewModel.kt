package com.example.battleshipmobile.battleship.play.lobby

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.play.lobby.QueueState
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.dto.LobbyInformationDTO
import com.example.battleshipmobile.battleship.service.game.GameService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class QueueViewModel(private val gameService: GameService): ViewModel() {

    var lobby by mutableStateOf(Queue(state = QueueState.SEARCHING_OPPONENT))
    private set

    private val _pendingMatch = MutableStateFlow<LobbyInformationDTO?>(null)
    val pendingMatch = _pendingMatch.asStateFlow()

    fun startGame(gameID: ID){
        lobby = Queue(state = QueueState.FULL, gameID = gameID)
    }

    fun enterLobby(): Job = viewModelScope.launch {
        gameService
            .pollLobbyInformation()
            .collectLatest {
                if(it.gameID != null) {
                    _pendingMatch.value = it
                }
            }
    }

    fun leaveLobby(){
        viewModelScope.launch {
            try{
                gameService.cancelPolling()
                Result.success(gameService.cancelQueue())
            }catch (e: Exception){
                Result.failure<LobbyInformationDTO>(e)
            }
        }
    }
}

data class Queue(val state: QueueState, val gameID: ID? = null){

    init{
        require(state != QueueState.FULL || gameID != null){
            "GameID must be provided when state is FULL"
        }
    }
}

