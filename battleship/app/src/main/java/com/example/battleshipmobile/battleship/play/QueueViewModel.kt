package com.example.battleshipmobile.battleship.play

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.UserID
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.UUID

const val MAX_NUM_PLAYERS = 2

class QueueViewModel(private val lobbyService: LobbyService): ViewModel() {
    enum class LoadingState{ LOADING, IDLE }

    var loadingState by mutableStateOf(LoadingState.IDLE)
    private set

    var lobbyInformation by mutableStateOf<LobbyInformation?>(null)
    private set

    val updateFlow = flow<LobbyInformation>{
        val startingValue = lobbyInformation ?: throw IllegalAccessError() //TODO remove throw
        var currentValue = startingValue

        while(currentValue.gameID == null){
            emit(currentValue)
            delay(500)
            val lobbyInfo = lobbyService.get(currentValue.id)
            require(lobbyInfo != null)
            currentValue = lobbyInfo
        }
    }

    fun enqueue(userToken: String){
        viewModelScope.launch {
            loadingState = LoadingState.LOADING

            lobbyInformation = lobbyService.enqueue(userToken)

            val currentLobbyInfo = lobbyInformation
            require(currentLobbyInfo != null)

            if(currentLobbyInfo.gameID == null){
                updateFlow
            }
            loadingState = LoadingState.IDLE
        }
    }

    fun collectFlow(){
        viewModelScope.launch {
            updateFlow.collectLatest {
                lobbyInformation = it
            }
        }
    }
}