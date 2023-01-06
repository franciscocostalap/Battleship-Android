package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.model.GameRules

data class ShipRulesDTO(
    val name: String,
    val fleetComposition: Map<String, Int>
)

fun ShipRulesDTO.toFleetComposition()=
    GameRules.FleetComposition(
        name = name,
        composition = fleetComposition.mapKeys { it.key.toInt() }
    )
