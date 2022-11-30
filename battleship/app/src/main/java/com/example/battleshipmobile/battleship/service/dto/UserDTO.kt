package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.user.User

/**
 * Data transfer object for [User]
 */
data class UserDTO(val username: String, val password: String)
fun UserDTO(user: User) = UserDTO(user.username.value, user.password.value)