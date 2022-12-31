package com.example.battleshipmobile.battleship.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.game.LobbyInformation
import com.example.battleshipmobile.battleship.service.game.GameService
import kotlinx.coroutines.launch

class HomeViewModel(private val gameService: GameService, private val authInfoRepository: AuthInfoService) : ViewModel() {


    var lobbyInformationResult by mutableStateOf<Result<LobbyInformation>?>(null)
    private set

    var uid by mutableStateOf<ID?>(null)
    private set


    fun enqueue(){
        viewModelScope.launch {
           lobbyInformationResult =  try{
               Result.success(gameService.enqueue())
           }catch (e: Exception){
               Result.failure(e)
           }
        }
    }

    fun setLobbyInfoToNull(){
        lobbyInformationResult = null
    }

    fun logout(){
        uid = authInfoRepository.uid
        authInfoRepository.clearAuthInfo()
    }

    fun isLoggedIn() =
        authInfoRepository.hasAuthInfo()
}