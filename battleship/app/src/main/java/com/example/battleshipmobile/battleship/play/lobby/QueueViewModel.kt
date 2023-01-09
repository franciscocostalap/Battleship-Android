package com.example.battleshipmobile.battleship.play.lobby

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.dto.LobbyInformationDTO
import com.example.battleshipmobile.battleship.service.game.GameService
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class QueueViewModel(private val gameService: GameService): ViewModel() {

    var lobbyInformationResult by mutableStateOf<Result<LobbyInformation>?>(null)
        private set
    var lobby by mutableStateOf<Queue?>(null)
        private set
    var isPolling by mutableStateOf(false)

    init {
        Log.v("QueueViewModel", "init")
        enterLobby()
    }

    fun waitForOpponent(lobbyInfo: LobbyInformation){
        lobby = Queue(gameID=lobbyInfo.gameID, lobbyID=lobbyInfo.lobbyID)
        viewModelScope.launch {
            isPolling = true
            gameService
                .pollLobbyInformation()
                .collectLatest {
                    if(it.gameID != null) {
                        lobby = Queue(it.gameID, it.lobbyID)
                    }
                }
            isPolling = false
        }
    }

    private fun enterLobby()  {
        viewModelScope.launch {
            lobbyInformationResult =  try{
                Result.success(gameService.enqueue())
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }


    fun leaveLobby(){
        viewModelScope.launch {
            try{
                gameService.cancelLobbyPolling()
                Result.success(gameService.cancelQueue())
            }catch (e: Exception){
                Result.failure<LobbyInformationDTO>(e)
            }
        }
    }

    fun startGame(lobbyInfo: LobbyInformation) {
        lobby = Queue(lobbyInfo.gameID, lobbyInfo.lobbyID)
    }
}

class Queue(val gameID: ID? = null, val lobbyID: ID? = null){

    val state = when(gameID){
        null -> QueueState.SEARCHING_OPPONENT
        else -> QueueState.FULL
    }

}

