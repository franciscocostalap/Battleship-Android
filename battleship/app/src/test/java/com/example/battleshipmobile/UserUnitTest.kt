package com.example.battleshipmobile

import com.example.battleshipmobile.battleship.service.user.User
import org.junit.Test

/**
 * Unit tests for user related operations and validations
 */
class UserUnitTest {

    @Test(expected = IllegalArgumentException::class)
    fun `user must have a non blank username`() {
        User("", "password")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `user must have a non blank password`() {
        User("username", "")
    }

}
