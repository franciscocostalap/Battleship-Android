package com.example.battleshipmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.*
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme


const val SQUARE_BASE_SIDE = 80

@Composable
fun BoardView(board: Board){
    val matrix = board.toMatrix()
    Column {
        for (c in 0 until board.side){
            Row{
                for (r in 0 until board.side){
                    val square = Square(r.row, c.column)
                    SquareView(type = matrix[square] ?: SquareType.Water, board.side)
                }
            }
        }
    }
}

@Composable
fun SquareView(type: SquareType, boardSide: Int){
    val bgColor = when(type){
        SquareType.Shot -> Color.Gray
        SquareType.ShipPart -> Color.White
        SquareType.Water -> Color.Blue
        SquareType.Hit -> Color.Red
    }

    val side = (200 / (0.50 * boardSide)).dp

    val boxModifier = Modifier
        .border(1.dp, Color.Black)
        .width(side)
        .height(side)
        .background(bgColor)

    Box(contentAlignment = Alignment.Center, modifier = boxModifier){

    }
}

@Preview
@Composable
fun EmptyBoardPreview(){
    val board = Board(
        5,
        emptyList(),
        emptyList(),
        emptyList()
    )

    BattleshipMobileTheme {
        BoardView(board = board)
    }
}

@Preview
@Composable
fun ExampleBoardPreview(){
    val board = Board(
        10,
        shots=listOf(Square(0, 0)),
        hits = listOf(Square(4, 2)),
        shipParts = listOf(Square(3, 2), Square(2, 2))
    )

    BattleshipMobileTheme {
        BoardView(board = board)
    }
}