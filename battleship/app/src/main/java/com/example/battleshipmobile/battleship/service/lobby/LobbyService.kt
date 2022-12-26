package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.ID

interface LobbyService {
    /**
     * Queues up a user to play returning the lobby information.
     *
     * @return [LobbyInformation]
     */
    suspend fun enqueue(): LobbyInformation

    /**
     * Gets the lobby information of the one that was requested.
     *
     * @param lobbyID id of the lobby to get
     * @return [LobbyInformation]
     */
    suspend fun get(lobbyID: ID): LobbyInformation

    /**
     * The user quits from the requested lobby
     *
     * @param lobbyID the id of the lobby
     */
    suspend fun cancel(lobbyID: ID)
}