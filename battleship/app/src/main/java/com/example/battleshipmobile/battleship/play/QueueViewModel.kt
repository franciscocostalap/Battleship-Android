package com.example.battleshipmobile.battleship.play


import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.game.LobbyInformation
import com.example.battleshipmobile.battleship.service.game.GameService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class QueueViewModel(private val gameService: GameService): ViewModel() {

    var lobbyInformation by mutableStateOf<LobbyInformation?>(null)

    var lobbyState by mutableStateOf(QueueState.SEARCHING_OPPONENT)
    private val DELAY_TIME = 1500L

    private fun lobbyInformationFlow(lobbyID: ID) = flow{
        var currentLobbyValue = fetchLobbyInfo(lobbyID)

        do{
            emit(currentLobbyValue)
            delay(DELAY_TIME)
            currentLobbyValue = fetchLobbyInfo(lobbyID)
        }while(currentLobbyValue.gameID == null)

        lobbyState = QueueState.FULL
    }

    private suspend fun fetchLobbyInfo(lobbyID: ID): LobbyInformation {
        return coroutineScope {
            val result = async {
                lobbyInformation = gameService.get(lobbyID)
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
                Result.success(gameService.cancel(lobbyID))
                lobbyInformation = null
            }catch (e: Exception){
                Result.failure<LobbyInformation>(e)
            }
        }
    }
}


