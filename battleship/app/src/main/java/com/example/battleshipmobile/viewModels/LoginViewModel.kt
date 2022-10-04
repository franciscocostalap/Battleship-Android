package com.example.battleshipmobile.viewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _name = mutableStateOf(TextFieldValue(""))
    val name: State<TextFieldValue>
        get() = _name

    private val _password = mutableStateOf(TextFieldValue(""))
    val password: State<TextFieldValue>
        get() = _password

    fun updatePassword(password: TextFieldValue){
        _password.value = password
    }

    fun updateName(login: TextFieldValue){
        _name.value = login
    }
}