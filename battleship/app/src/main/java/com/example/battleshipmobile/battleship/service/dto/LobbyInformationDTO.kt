package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.lobby.LobbyInformation

data class LobbyInformationDTO(
    val lobbyID: ID,
    val gameID: ID?
)

/**
 * Converts DTO to model
 */
fun LobbyInformationDTO.toLobbyInformation() = LobbyInformation(
    lobbyID = lobbyID,
    gameID = gameID
)