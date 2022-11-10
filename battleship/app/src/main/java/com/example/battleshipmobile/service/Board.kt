package com.example.battleshipmobile

data class Board(
    val side: Int,
    val shots: List<Square>,
    val hits: List<Square>,
    val shipParts: List<Square>
)

enum class SquareType{
    Shot,
    Hit,
    ShipPart,
    Water
}

fun Board.toMatrix(): Map<Square, SquareType>{
    return listOf(
        hits.map { it to SquareType.Hit },
        shots.map { it to SquareType.Shot } ,
        shipParts.map { it to SquareType.ShipPart }
    ).flatten()
        .toMap()
}

data class Square(val row: Row, val column: Column)

fun Square(rowOrdinal: Int, columnOrdinal: Int) = Square(rowOrdinal.row, columnOrdinal.column)

data class Row(val ordinal: Int)
data class Column(val ordinal: Int)

val Int.row get() = Row(this)
val Int.column get() = Column(this)

