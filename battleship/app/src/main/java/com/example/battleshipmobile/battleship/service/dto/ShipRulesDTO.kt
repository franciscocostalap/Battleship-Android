package com.example.battleshipmobile.battleship.service.dto

data class ShipRulesDTO(
    val name: String,
    val fleetComposition: Map<String, Int>
)