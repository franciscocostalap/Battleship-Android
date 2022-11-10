package com.example.battleshipmobile.battleship.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.service.*
import com.example.battleshipmobile.utils.Problem
import kotlinx.coroutines.launch

class LoginViewModel(private val userService: UserService): ViewModel() {

    enum class LoadingState{ LOADING, IDLE}

    var loadingState by mutableStateOf(LoadingState.IDLE)
    private set

    var authInformation by mutableStateOf<AuthInfo?>(null)
    private set

    fun login(authenticator: User){
        viewModelScope.launch {
            loadingState = LoadingState.LOADING
            val result = userService.login(authenticator)
            val authInfo = when(result){
                is ValueResult -> result.value
                is ProblemResult ->  throw Exception()// TODO
            }
            authInformation = authInfo
            loadingState = LoadingState.IDLE
        }
    }

}