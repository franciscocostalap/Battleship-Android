package com.example.battleshipmobile.battleship.service.user

interface UserService{

    /**
     * Registers a new user returning it's authentication information
     */
    suspend fun register(user: User): AuthInfo

    /**
     * Authenticates an already existing user returning it's authentication information
     */
    suspend fun login(authenticator: User): AuthInfo

}