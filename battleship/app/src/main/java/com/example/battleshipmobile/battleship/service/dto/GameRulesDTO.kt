package com.example.battleshipmobile.battleship.service.dto

data class GameRulesDTO(
    val boardSide: Int,
    val shotsPerTurn: Int,
    val layoutDefinitionTimeout: Long,
    val playTimeout: Long,
    val shipRules: ShipRulesDTO
)