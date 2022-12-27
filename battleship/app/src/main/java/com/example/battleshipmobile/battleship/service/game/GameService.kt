package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.service.ID

interface GameService {
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

    /**
     * Places the given ships on the board.
     *
     * @param layout The ships to place on the board.
     */
    suspend fun placeShips(layout: ShipsInfoDTO)
}