package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.UserID

interface LobbyService {
    /**
     * Enqueues a user and starts the game if the lobby is already full
     * @param userToken token of the user to enqueue
     * @return [LobbyInformation]
     */
    suspend fun enqueue(userToken: String): LobbyInformation

    /**
     *
     */
    suspend fun get(lobbyID: ID, userToken: String): LobbyInformation?

    /**
     *
     */
    suspend fun cancel(lobbyID: ID, userToken: String)
}