package com.example.battleshipmobile.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _email = mutableStateOf(TextFieldValue(""))
    val email: State<TextFieldValue>
        get() = _email

    private val _password = mutableStateOf(TextFieldValue(""))
    val password: State<TextFieldValue>
        get() = _password

    fun updatePassword(password: TextFieldValue){
        _password.value = password
    }

    fun updateEmail(login: TextFieldValue){
        _email.value = login
    }
}