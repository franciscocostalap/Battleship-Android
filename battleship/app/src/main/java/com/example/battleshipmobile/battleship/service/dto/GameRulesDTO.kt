package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.model.GameRules

data class GameRulesDTO(
    val boardSide: Int,
    val shotsPerTurn: Int,
    val layoutDefinitionTimeout: Long,
    val playTimeout: Long,
    val shipRules: ShipRulesDTO
)


/**
 * Converts DTO to model
 */
fun GameRulesDTO.toGameRules() = GameRules(
    boardSide = boardSide,
    shotsPerTurn = shotsPerTurn,
    layoutDefinitionTime = layoutDefinitionTimeout,
    playTimeout = playTimeout,
    fleetComposition = shipRules.toFleetComposition()
)