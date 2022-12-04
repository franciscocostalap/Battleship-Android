package com.example.battleshipmobile.battleship.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.lobby.LobbyService
import kotlinx.coroutines.launch

class HomeViewModel(private val lobbyService: LobbyService) : ViewModel() {


    var lobbyInformationResult by mutableStateOf<Result<LobbyInformation>?>(null)
    private set

    fun enqueue(userToken: String){
        viewModelScope.launch {
           lobbyInformationResult =  try{
               Result.success(lobbyService.enqueue(userToken))
           }catch (e: Exception){
               Result.failure(e)
           }
        }
    }

    fun setLobbyInfoToNull(){
        lobbyInformationResult = null
    }
}