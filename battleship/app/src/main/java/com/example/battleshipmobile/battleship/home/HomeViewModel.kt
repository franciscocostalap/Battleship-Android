package com.example.battleshipmobile.battleship.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.auth.AuthInfoService
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.dto.LobbyInformationDTO
import com.example.battleshipmobile.battleship.service.game.GameService
import kotlinx.coroutines.launch

class HomeViewModel(private val authInfoRepository: AuthInfoService) : ViewModel() {

    var uid by mutableStateOf<ID?>(null)
    private set

    fun logout(){
        uid = authInfoRepository.uid
        authInfoRepository.clearAuthInfo()
    }

    fun isLoggedIn() =
        authInfoRepository.hasAuthInfo()
}