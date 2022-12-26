package com.example.battleshipmobile.battleship.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.UserService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AuthViewModel(private val userService: UserService): ViewModel() {

    var retrievedAuthInformation by mutableStateOf<Result<AuthInfo>?>(null)
    private set

    var authFormType by mutableStateOf(AuthenticationFormType.Login)
    private set

    fun login(user: User) = authenticate {try{
        userService.login(user)
    }catch (e: Exception){
        Log.e("AuthViewModel", "Error authenticating user", e)
        throw e
    } }

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
        try {
            viewModelScope.launch() {
                retrievedAuthInformation = try {
                    val result = authInfoSupplier()
                    Result.success(result)
                }catch (e: Throwable){
                    Log.v("AuthViewModel", "Error: ${e.message}")
                    Result.failure(e)
                }
                Log.v("AuthViewModel", "Result: ${retrievedAuthInformation}")
            }
        }catch (e: Exception){
            Log.v("AuthViewModel", "Error: ${e.message}")
        }
    }

}
