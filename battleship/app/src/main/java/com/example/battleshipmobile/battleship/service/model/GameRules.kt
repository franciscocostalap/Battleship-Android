package com.example.battleshipmobile.battleship.service.model

data class GameRules(
    val boardSide: Int,
    val layoutDefinitionTime: Long,
    val shotsPerTurn: Int,
    val fleetComposition: FleetComposition,
    val playTimeout: Long
){

    data class FleetComposition(
        val name: String,

        /**
         *  The number of ships of each type in the fleet
         */
        val composition: Map<Int /* ShipSize */, Int /* ShipCount */>
    )


}