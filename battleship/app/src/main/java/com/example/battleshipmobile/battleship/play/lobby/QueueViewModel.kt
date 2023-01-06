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
    private var lobbyMonitor : Job? = null

    private val _pendingMatch = MutableStateFlow<LobbyInformation?>(null)

    val pendingMatch = _pendingMatch.asStateFlow()

    fun waitForMatch() = viewModelScope.launch {
        if(lobbyMonitor != null){
            lobbyMonitor?.join()
            val lobbyInfo = lobbyInformationResult?.getOrNull()
            if(lobbyInfo?.gameID != null){
                Log.v("QUEUE_VIEW_MODEL", "Lobby was found at the start")
                _pendingMatch.value = lobbyInfo
                return@launch
            }
            if(lobbyInformationResult?.isFailure == true){
                Log.e("QUEUE_VIEW_MODEL", "Error joining match")
                return@launch
            }
        }else throw IllegalStateException("Lobby was not created")
        gameService
            .pollLobbyInformation()
            .collectLatest {

                if(it.gameID != null) {
                    _pendingMatch.value = it
                }
            }
    }

    fun enterLobby()  {
        if(lobbyMonitor != null) { //prevent multiple lobby calls
            return
        }
        lobbyMonitor = viewModelScope.launch {
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
}

data class Queue(val state: QueueState, val gameID: ID? = null, val lobbyID: ID? = null)

