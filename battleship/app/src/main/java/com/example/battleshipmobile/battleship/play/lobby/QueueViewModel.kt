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

    private val _pendingMatch = MutableStateFlow<LobbyInformation?>(null)
    var isPooling by mutableStateOf(false)


    private fun waitForMatch() = viewModelScope.launch {
        gameService
            .pollLobbyInformation()
            .collectLatest {

                if(it.gameID != null) {
                    _pendingMatch.value = it
                }
            }
    }

    fun startPollingLobby(){
        viewModelScope.launch {
            waitForMatch()
            Log.v("QUEUE_VIEW_MODEL", "starting to collect")
            _pendingMatch.collectLatest {
                if (it != null) {
                    Log.v("QUEUE_VIEW_MODEL", "Match found")
                    lobby = Queue(QueueState.FULL, gameID = it.gameID, lobbyID = it.lobbyID)
                }
            }
        }
    }

    fun enterLobby()  {
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
}

data class Queue(val state: QueueState, val gameID: ID? = null, val lobbyID: ID? = null)

