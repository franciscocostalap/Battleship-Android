package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.ID

data class LobbyInformationDTO(
    val lobbyID: ID,
    val gameID: ID?
)