package com.example.battleshipmobile.battleship.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.UserService
import kotlinx.coroutines.launch

class LoginViewModel(private val userService: UserService): ViewModel() {

    enum class LoadingState{ LOADING, IDLE }

    var loadingState by mutableStateOf(LoadingState.IDLE)
    private set

    var authInformation by mutableStateOf<Result<AuthInfo>?>(null)
    private set

    fun login(user: User){
        viewModelScope.launch {
            try{
                loadingState = LoadingState.LOADING
                authInformation = Result.success(userService.login(user))
            }catch (e: Exception){
                authInformation = Result.failure(e)
            }
            loadingState = LoadingState.IDLE
        }
    }

    fun logout(){
        authInformation = null
    }

}
