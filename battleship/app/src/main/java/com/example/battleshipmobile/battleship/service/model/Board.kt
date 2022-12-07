package com.example.battleshipmobile.battleship.service.model

import java.util.*

data class Board(
    val side: Int,
    val shots: List<Square>,
    val hits: List<Square>,
    val shipParts: List<Square>
){
    companion object
}

enum class SquareType{
    Shot,
    Hit,
    ShipPart,
    Water
}

fun Board.Companion.empty(side: Int): Board {
    return Board(
        side = side,
        shots = listOf(),
        hits = listOf(),
        shipParts = listOf()
    )
}

fun Board.placeShip(initialSquare: Square, ship: Ship, checkConstraints: Boolean=true): Board{

    val shipSquares = ship.getSquares(initialSquare)

    if(checkConstraints && !canPlaceShip(shipSquares)) return this

    return copy(
        shipParts = shipParts + shipSquares
    )
}

/**
 * Returns true if the ship specified by the [shipSquares] can be placed on the [Board]
 *
 * A ship can be placed if:
 * - It is entirely contained in the board
 * - It does not overlap with any other ship
 * - It does not have any adjacent ship parts
 *
 * @param shipSquares the squares that the ship occupies
 * @return true if the ship can be placed on the [Board]
 */
private fun Board.canPlaceShip(shipSquares: List<Square>): Boolean {

    val unplaceableSquare = shipSquares.find {
         shipParts.contains(it) || !isInBounds(it)
    }

    return unplaceableSquare == null && !hasAdjacentShips(shipSquares)
}

/**
 * Searches around the given squares to prevent adjacent ships
 * @param shipSquares the ship parts
 * @return true if there are adjacent ships
 */
private fun Board.hasAdjacentShips(shipSquares: List<Square>): Boolean {
    val seen = mutableListOf<Square>()
    val unchecked = LinkedList(shipSquares)
    seen.addAll(shipSquares)

    while (unchecked.isNotEmpty()) {
        val square = unchecked.removeFirst()
        val neighbours = square.getSurrounding().filter { isInBounds(it) }
        neighbours.forEach {
            if (it !in seen) {
                if (shipParts.contains(it)) {
                    return true
                }
                seen.add(it)
            }
        }
    }

    return false
}

fun Board.isInBounds(square: Square): Boolean
    = square.row.ordinal in 0 until side && square.column.ordinal in 0 until side

fun Board.toMatrix(): Map<Square, SquareType>{
    return listOf(
        hits.map { it to SquareType.Hit },
        shots.map { it to SquareType.Shot } ,
        shipParts.map { it to SquareType.ShipPart }
    ).flatten()
     .toMap()
}
