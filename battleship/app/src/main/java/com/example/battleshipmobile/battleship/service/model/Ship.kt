package com.example.battleshipmobile.battleship.service.model

import android.util.Log

data class Ship(val size: Int, val orientation: Orientation)

fun Ship.getSquares(initial: Square): List<Square>{
    val offsetRange = 0 until size
    Log.v("ShipGetSquares", "initial: $initial, size: $size, orientation: $orientation")
    return when (orientation) {
        Orientation.Horizontal -> offsetRange.map { initial.copy(column = initial.column + it) }
        Orientation.Vertical -> offsetRange.map { initial.copy(row = initial.row + it) }
    }
}

enum class Orientation{
    Horizontal,
    Vertical
}

fun Orientation.opposite(): Orientation{
    return when(this){
        Orientation.Horizontal -> Orientation.Vertical
        Orientation.Vertical -> Orientation.Horizontal
    }
}

