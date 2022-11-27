package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.ID

interface LobbyService {
    /**
     * Enqueues a user and starts the game if the lobby is already full
     * @param userToken user to enqueue
     * @return [LobbyInformation]
     */
    suspend fun enqueue(userToken: String): LobbyInformation

    /**
     *
     */
    suspend fun getLobbyInfo(lobbyID: ID): LobbyInformation?
}