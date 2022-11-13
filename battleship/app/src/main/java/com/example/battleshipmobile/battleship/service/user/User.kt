package com.example.battleshipmobile.battleship.service.user



data class User(val username: String, val password: String){

    companion object{
        const val MIN_USERNAME_LENGTH = 3
        const val MIN_PASSWORD_LENGTH = 8
        val validValidationPair = Pair(UsernameValidation.VALID, PasswordValidation.VALID)
    }

    init {
        require(
            validateUserInfo(username, password) == validValidationPair
        )
    }

}

enum class UsernameValidation{
    VALID,
    EMPTY,
    TOO_SHORT
}

enum class PasswordValidation{
    VALID,
    EMPTY,
    TOO_SHORT,
    NO_SPECIAL_CHARACTERS,
    NO_DIGITS,
}

/**
 * Validates the user information
 * @param username the username
 * @param password the password
 *
 * @return the validation result which gives semantic information about why the validation failed
 * or [UsernameValidation.VALID] and [PasswordValidation.VALID] if the validation was successful
 */
fun validateUserInfo(username: String, password: String): Pair<UsernameValidation, PasswordValidation>{

    val usernameValidation = when{
        username.isEmpty() -> UsernameValidation.EMPTY
        username.length < User.MIN_USERNAME_LENGTH -> UsernameValidation.TOO_SHORT
        else -> UsernameValidation.VALID
    }

    val passwordValidation = when{
        password.isEmpty() -> PasswordValidation.EMPTY
        password.length < User.MIN_PASSWORD_LENGTH -> PasswordValidation.TOO_SHORT
        !password.any { it.isDigit() } -> PasswordValidation.NO_DIGITS
        password.all { it.isLetterOrDigit() } -> PasswordValidation.NO_SPECIAL_CHARACTERS
        else -> PasswordValidation.VALID
    }

    return Pair(usernameValidation, passwordValidation)
}

/**
 *
 */
data class AuthInfo(val uid: Int, val token: String)



