package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.ID

interface LobbyService {
    /**
     * Queues up a user to play returning the lobby information.
     *
     * @param userToken token of the user performing the action
     * @return [LobbyInformation]
     */
    suspend fun enqueue(userToken: String): LobbyInformation

    /**
     * Gets the lobby information of the one that was requested.
     *
     * @param lobbyID id of the lobby to get
     * @param userToken token of the user performing the action
     * @return [LobbyInformation]
     */
    suspend fun get(lobbyID: ID, userToken: String): LobbyInformation

    /**
     * The user quits from the requested lobby
     *
     * @param lobbyID the id of the lobby
     * @param userToken token of the user performing the action
     */
    suspend fun cancel(lobbyID: ID, userToken: String)
}