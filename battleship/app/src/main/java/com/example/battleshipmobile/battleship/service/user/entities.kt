package com.example.battleshipmobile.battleship.service.user



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
data class AuthInfo(val uid: Int, val token: String)
