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

    fun login(username: String, password: String){
        viewModelScope.launch {
            authInformation = try{
                loadingState = LoadingState.LOADING
                Result.success(userService.login(User(username, password)))
            }catch (e: Exception){
                Result.failure(e)
            }
            loadingState = LoadingState.IDLE
        }
    }

}
