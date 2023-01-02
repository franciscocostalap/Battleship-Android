package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.service.dto.GameRulesDTO
import com.example.battleshipmobile.battleship.service.dto.GameStateInfoDTO
import com.example.battleshipmobile.battleship.service.dto.LobbyInformationDTO
import com.example.battleshipmobile.battleship.service.dto.ShipsInfoDTO
import kotlinx.coroutines.flow.Flow

interface GameService {
    /**
     * Queues up a user to play returning the lobby information.
     *
     * @return [LobbyInformationDTO]
     */
    suspend fun enqueue(): LobbyInformationDTO

    /**
     * Gets the lobby information of the one that was requested.
     *
     * @return [LobbyInformationDTO]
     */
    suspend fun getLobbyInformation(): LobbyInformationDTO

    /**
     * Gets the game rules of the game that was requested.
     */
    suspend fun getGameRules(): GameRulesDTO

    /**
     * Gets the game state information of the game that was requested.
     */
    suspend fun getGameStateInfo(): GameStateInfoDTO

    /**
     * The user quits from the requested lobby
     */
    suspend fun cancel()

    /**
     * Places the given ships on the board.
     *
     * @param layout The ships to place on the board.
     */
    suspend fun placeShips(layout: ShipsInfoDTO)

    /**
     * Keeps polling the game state [GameStateInfoDTO].
     *
     * @return the flow of the game state
     */
    suspend fun pollGameStateInfo(): Flow<GameStateInfoDTO>

    /**
     * Keeps polling the lobby information [LobbyInformationDTO].
     *
     * @return the flow of the lobby information
     */
    suspend fun pollLobbyInformation(): Flow<LobbyInformationDTO>

    /**
     * Cancels the current polling
     */
    fun cancelPolling()
}