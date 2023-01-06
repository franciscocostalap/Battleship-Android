package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.model.GameStateInfo
import com.example.battleshipmobile.battleship.service.model.State


data class GameStateInfoDTO(
    val state: State,
    val turnID: ID,
    val player1ID: ID,
    val player2ID: ID,
    val remainingTime: Long
)

/**
 * Converts DTO to domain object
 */
fun GameStateInfoDTO.toGameStateInfo() = GameStateInfo(
    state = state,
    turnID = turnID,
    player1ID = player1ID,
    player2ID = player2ID,
    remainingTime = 0
)