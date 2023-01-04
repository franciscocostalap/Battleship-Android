package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.model.Orientation
import com.example.battleshipmobile.battleship.service.model.Square

data class ShipDTO(val initialSquare: Square, val size: Int, val orientation: Orientation)