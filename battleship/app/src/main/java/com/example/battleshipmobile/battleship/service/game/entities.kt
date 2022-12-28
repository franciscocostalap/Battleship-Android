package com.example.battleshipmobile.battleship.service.game

import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.model.Orientation
import com.example.battleshipmobile.battleship.service.model.Square

data class LobbyInformation(
    val lobbyID: ID,
    val gameID: ID?
)

data class GameStateInfo(val state: State, val turnID: ID, val player1ID: ID, val player2ID: ID)

enum class State { PLACING_SHIPS, PLAYING, FINISHED, CANCELLED }

data class ShipsInfoDTO(val shipsInfo: List<ShipDTO>)
data class ShipDTO(val initialSquare: Square, val size: Int, val orientation: Orientation)