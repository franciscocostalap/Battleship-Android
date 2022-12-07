package com.example.battleshipmobile.battleship.service.model

/**
 * Represents a Square
 */
data class Square(val row: Row, val column: Column){
    constructor(rowOrdinal: Int, columnOrdinal: Int) : this(Row(rowOrdinal), Column(columnOrdinal))
}

data class Row(val ordinal: Int)
data class Column(val ordinal: Int)

operator fun Row.plus(offset: Int): Row = Row(ordinal + offset)
operator fun Column.plus(offset: Int): Column = Column(ordinal + offset)

val Int.row get() = Row(this)
val Int.column get() = Column(this)

/**
 * Gets the neighbours of a square on the y-axis and x-axis
 *
 * @receiver the square to get the neighbours from
 * @return [List] containing the vertical and horizontal neighbours of the square
 */
fun Square.getAxisNeighbours(): List<Square> {
    val top = Square((row.ordinal - 1), column.ordinal)
    val bottom = Square((row.ordinal + 1), column.ordinal)
    val left = Square(row.ordinal, (column.ordinal - 1))
    val right = Square(row.ordinal, (column.ordinal + 1))

    return listOfNotNull(
        top, bottom, left, right
    )
}

/**
 * Gets the neighbours of a square
 * @return List<com.example.battleshipmobile.battleship.service.model.Square> diagonal, vertical and horizontal neighbours of the ship
 */
fun Square.getSurrounding() : List<Square> = this.getAxisNeighbours() + this.getDiagonals()

/**
 * Gets the diagonal neighbours of a square
 * @return List<com.example.battleshipmobile.battleship.service.model.Square> containing the diagonal neighbours of the ship
 */
fun Square.getDiagonals(): List<Square> {
    val topLeft = Square((row.ordinal - 1), (column.ordinal - 1))
    val topRight = Square((row.ordinal - 1), (column.ordinal + 1))
    val bottomLeft = Square((row.ordinal + 1), (column.ordinal - 1))
    val bottomRight = Square((row.ordinal + 1), (column.ordinal + 1))

    return listOf(
        topLeft, topRight, bottomLeft, bottomRight
    )
}