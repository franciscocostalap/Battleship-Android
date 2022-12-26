package com.example.battleshipmobile

import com.example.battleshipmobile.battleship.service.user.Password
import com.example.battleshipmobile.battleship.service.user.Username
import com.example.battleshipmobile.battleship.service.user.validate
import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Unit tests for user related operations and validations
 */
class UserUnitTest {

    @Test(expected = IllegalArgumentException::class)
    fun `Empty Username validation returns all validation errors`(){
        val usernameValidation = Username.validate("")
        val expected = setOf(
            Username.Validation.EMPTY,
            Username.Validation.TOO_SHORT
        )

        assertEquals(expected, usernameValidation)
        Username("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Username with whitespace validation returns whitespace validation error`(){
        val usernameValidation = Username.validate("test user")
        val expected = setOf(
            Username.Validation.NO_WHITESPACE_TABS
        )

        assertEquals(expected, usernameValidation)
        Username("test user")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Empty Password returns all validation errors`(){
        val passwordValidation = Password.validate("")
        val expected = setOf(
            Password.Validation.EMPTY,
            Password.Validation.TOO_SHORT,
        )

        assertEquals(expected, passwordValidation)
        Password("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Password too short`(){
        val sut = "pass@1"
        val passwordValidation = Password.validate(sut)
        val expected = setOf(
            Password.Validation.TOO_SHORT
        )

        assertEquals(expected, passwordValidation)
        Password(sut)
    }

    @Test
    fun `Password with all requirements`(){
        val sut = "password@1"
        val passwordValidation = Password.validate(sut)
        val expected = emptySet<Password.Validation>()

        assertEquals(expected, passwordValidation)
    }

    @Test
    fun `Empty Password with gives all errors except ignored`(){
        val passwordValidation = Password.validate("",
            ignore = setOf(Password.Validation.EMPTY, Password.Validation.TOO_SHORT)
        )
        val expected = emptySet<Password.Validation>()

        assertEquals(expected, passwordValidation)
    }


}
