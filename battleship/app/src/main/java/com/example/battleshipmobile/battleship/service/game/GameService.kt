package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.play.shotDefinition.GameTurn
import com.example.battleshipmobile.battleship.service.AppService
import com.example.battleshipmobile.battleship.service.dto.*
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.GameRules
import com.example.battleshipmobile.battleship.service.model.GameStateInfo
import kotlinx.coroutines.flow.Flow

interface GameService : AppService {

    /**
     * Queues up a user to play returning the lobby information.
     *
     * @return [LobbyInformationDTO]
     */
    suspend fun enqueue(): LobbyInformation

    /**
     * Gets the lobby information of the one that was requested.
     *
     * @return [LobbyInformationDTO]
     */
    suspend fun getLobbyInformation(): LobbyInformation

    /**
     * Gets the game rules of the game that was requested.
     */
    suspend fun getGameRules(): GameRules

    /**
     * Gets the game state information of the game that was requested.
     */
    suspend fun getGameStateInfo(): GameStateInfo

    /**
     * The user quits from the requested lobby
     */
    suspend fun cancelQueue()

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
    suspend fun pollGameStateInfo(): Flow<GameStateInfo>

    /**
     * Keeps polling the lobby information [LobbyInformationDTO].
     *
     * @return the flow of the lobby information
     */
    suspend fun pollLobbyInformation(): Flow<LobbyInformation>

    /**
     * Cancels the current polling
     */
    fun cancelPolling()

    /**
     * Gets the board of one of the players
     * @whichFleet which fleet to get the board from
     * @return [BoardDTO]
     */
    suspend fun getBoard(whichFleet: GameTurn): Board

    /**
     * Makes shots to a board
     * @param shotsDefinitionDTO list of shots to be made
     * @return [BoardDTO] the new board after the shots
     */
    suspend fun makeShots(shotsDefinitionDTO: ShotsDefinitionDTO): Board
}