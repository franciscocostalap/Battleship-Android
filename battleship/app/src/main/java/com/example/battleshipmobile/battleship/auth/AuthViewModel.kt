package com.example.battleshipmobile.battleship.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.UserService
import kotlinx.coroutines.launch

class AuthViewModel(private val userService: UserService): ViewModel() {

    var retrievedAuthInformation by mutableStateOf<Result<AuthInfo>?>(null)
    private set

    var authFormType by mutableStateOf(AuthenticationFormType.Login)
    private set

    fun login(user: User) = authenticate { userService.login(user) }

    fun register(user: User) = authenticate { userService.register(user) }

    fun clearAuthResult(){
        retrievedAuthInformation = null
    }
    fun swapFormType(){
        authFormType = when(authFormType){
            AuthenticationFormType.Register -> AuthenticationFormType.Login
            AuthenticationFormType.Login -> AuthenticationFormType.Register
        }
    }

    private fun authenticate(authInfoSupplier: suspend () -> AuthInfo){
        viewModelScope.launch {
            retrievedAuthInformation = try{
                Result.success(authInfoSupplier())
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }

}
