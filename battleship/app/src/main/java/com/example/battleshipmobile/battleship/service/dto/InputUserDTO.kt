package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.user.User

/**
 * Input data transfer object for [User]
 */
data class InputUserDTO(val username: String, val password: String)
fun InputUserDTO(user: User) = InputUserDTO(user.username.value, user.password.value)

/**
 * Output data transfer object for [User]
 */
data class OutputUserDTO(val name: String)
fun OutputUserDTO(user: User) = OutputUserDTO(user.username.value)
