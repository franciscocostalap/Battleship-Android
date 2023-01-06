package com.example.battleshipmobile.battleship.service.lobby

import com.example.battleshipmobile.battleship.service.ID

data class LobbyInformation(
    val lobbyID: ID,
    val gameID: ID?
)