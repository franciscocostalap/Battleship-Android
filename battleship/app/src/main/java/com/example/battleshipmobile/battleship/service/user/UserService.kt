package com.example.battleshipmobile.battleship.service.user

import com.example.battleshipmobile.battleship.service.AppService

interface UserService : AppService {

    /**
     * Registers a new user returning it's authentication information
     */
    suspend fun register(user: User): AuthInfo

    /**
     * Authenticates an already existing user returning it's authentication information
     */
    suspend fun login(authenticator: User): AuthInfo

}