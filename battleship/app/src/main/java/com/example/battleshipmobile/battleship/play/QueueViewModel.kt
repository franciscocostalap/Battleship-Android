package com.example.battleshipmobile.battleship.play


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class QueueViewModel(private val lobbyService: LobbyService): ViewModel() {

    private var lobbyInformation by mutableStateOf<LobbyInformation?>(null)
    private val DELAY_TIME = 1500L

    private val flow = MutableStateFlow<LobbyInformation?>(null)
    val stateFlow: StateFlow<LobbyInformation?> = flow.asStateFlow()

    private fun lobbyInformationFlow(lobbyID: ID) = flow{
        var currentLobbyValue = fetchLobbyInfo(lobbyID)

        do{
            emit(currentLobbyValue)
            delay(DELAY_TIME)
            currentLobbyValue = fetchLobbyInfo(lobbyID)
        }while(currentLobbyValue.gameId == null)
    }

    private suspend fun fetchLobbyInfo(lobbyID: ID): LobbyInformation {
        stateFlow
        return coroutineScope {
            val result = async {
                lobbyInformation = lobbyService.get(lobbyID)
                lobbyInformation
            }
            return@coroutineScope result.await() ?:
            throw IllegalArgumentException("LobbyInformation is null")
        }
    }


    fun waitForFullLobby(lobbyID: ID, onContinuation: suspend ()-> Unit){
        viewModelScope.launch {
            lobbyInformationFlow(lobbyID).collectLatest {
                lobbyInformation ?: cancel()
                lobbyInformation = it
            }

            Log.v("QUEUE_FLOW", "Opponent joined the lobby")
            onContinuation()
        }
    }

    fun cancelQueue(lobbyID: ID){
        viewModelScope.launch {
            try{
                Result.success(lobbyService.cancel(lobbyID))
                lobbyInformation = null
            }catch (e: Exception){
                Result.failure<LobbyInformation>(e)
            }
        }
    }
}


