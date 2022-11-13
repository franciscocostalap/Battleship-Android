package com.example.battleshipmobile.battleship.service.user

import com.example.battleshipmobile.battleship.service.UserID


sealed class UserModelException: Exception()
object UserIsBlankException: UserModelException()
object PasswordIsBlankException: UserModelException()

/**
 *
 */
data class User(val username: String, val password: String){

    init {
        if(username.isBlank()) throw UserIsBlankException
        if(password.isBlank()) throw PasswordIsBlankException
    }

}

/**
 *
 */
data class AuthInfo(val uid: UserID, val token: String)
