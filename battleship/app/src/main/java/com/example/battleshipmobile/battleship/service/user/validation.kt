package com.example.battleshipmobile.battleship.service.user


/**
 * Represents a username
 *
 * @property value the username as a string
 *
 * @throws IllegalArgumentException if the username is invalid regarding the validation rules
 *
 * Validations:
 * - must not be blank
 * - must be at least 3 characters long
 * - must not contain any whitespace
 *
 */
data class Username(val value: String){
    companion object{
        const val MIN_LENGTH = 3
    }

    enum class Validation(val validate: (String) -> Boolean){
        EMPTY({ it.isBlank() }),
        NO_WHITESPACE_TABS({ it.contains(Regex(" ")) }),
        TOO_SHORT({ it.length < MIN_LENGTH })
    }

    init {
        require(validate(value).isEmpty())
    }
}

/**
 * Validates a username and returns a [Username.Validation] enum value
 *
 * Can be used to check if a username is valid without instantiating a [Username] object
 * which throws an exception if the username is invalid.
 *
 * @param value The username [String] to validate
 * @return The [Username.Validation] enum value
 */
fun Username.Companion.validate(
    value: String,
    ignore: Set<Username.Validation> = emptySet()
): Set<Username.Validation> =
    Username.Validation
        .values()
        .filter { it !in ignore && it.validate(value) }
        .toSet()

/**
 * Represents a password
 *
 * @property value the password as a string
 * @throws IllegalArgumentException if the password is invalid regarding the validation rules
 *
 * Validations:
 * - must not be blank
 * - must be at least 8 characters long
 * - must contain at least one digit
 * - must contain at least one special character
 */
data class Password(val value: String){

    companion object{
        const val MIN_LENGTH = 8

        val SPECIAL_CHARACTERS = setOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '-', '_', '+', '=', '{', '}', '[', ']', '|', ':', ';', '<', '>', '?', '/', '.')
    }

    enum class Validation(val validate: (String) -> Boolean){
        EMPTY ({ pwd -> pwd.isBlank() }),
        TOO_SHORT ({ pwd -> pwd.length < MIN_LENGTH }),
        NO_DIGITS ({ pwd -> pwd.none { char -> char.isDigit() } }),
        NO_SPECIAL_CHARACTERS({ pwd -> pwd.none { char -> char in SPECIAL_CHARACTERS } })
    }

    init {
        require(validate(value).isEmpty())
    }
}

/**
 * Validates a password and returns a [Password.Validation] enum value
 *
 * Can be used to check if a password is valid without instantiating a [Password] object
 * which throws an exception if the password is invalid.
 *
 * @param value The password [String] to validate
 * @return The [Password.Validation] enum value
 */
fun Password.Companion.validate(
    value: String,
    ignore: Set<Password.Validation> = emptySet()
): Set<Password.Validation> = Password.Validation.values()
        .filter { it !in ignore && it.validate(value) }
        .toSet()


/**
 *
 */
data class AuthInfo(val uid: Int, val token: String)


/**
 * Represents the information to be used for authentication
 * @param username the username of the user
 * @param password the password of the user
 */
data class User(val username: Username, val password: Password)

/**
 * Instantiate a user with the given username and password as strings
 * @param username the username of the user
 * @param password the password of the user
 *
 * @throws IllegalArgumentException if the username or password are invalid
 * regarding its own validation rules
 */
fun User(username: String, password: String) = User(Username(username), Password(password))

