package com.example.battleshipmobile.ui.views.game

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.service.model.*
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.theme.yellow200
import com.example.battleshipmobile.ui.theme.yellow400


const val SQUARE_BASE_SIDE = 170
const val SQUARE_SHRINK_FACTOR = 0.5
const val SQUARE_BORDER_WIDTH = 1
val GRAY_4 = Color( 206, 212, 218)
val GRAY_6 = Color( 134, 142, 150)

@Composable
fun BoardView(
    board: Board,
    modifier: Modifier = Modifier,
    onSquareClicked: (Square) -> Unit,
) {
    val matrix = board.toMatrix()
    Column(modifier = modifier) {
        for (r in 0 until board.side) {
            Row {
                for (c in 0 until board.side) {
                    val square = Square(r.row, c.column)
                    SquareView(
                        type = matrix[square] ?: SquareType.Water,
                        boardSide = board.side,
                        onClick = { onSquareClicked(square) },
                    )
                }
            }
        }
    }
}

fun squareSide(side: Int): Double {
    return  SQUARE_BASE_SIDE / (SQUARE_SHRINK_FACTOR * side)
}

@Composable
fun SquareView(
    type: SquareType,
    boardSide: Int,
    onClick: () -> Unit = {}
) {

    val background = when (type) {
        SquareType.Shot -> GRAY_6
        SquareType.ShipPart -> INDIGO_7
        SquareType.Water -> GRAY_4
        SquareType.Hit -> Color.Red
        SquareType.AimedShot -> yellow400
    }

    val side = squareSide(boardSide).dp

    val boxModifier = Modifier
        .border(SQUARE_BORDER_WIDTH.dp, GRAY_8)
        .width(side)
        .height(side)
        .clickable(onClick = onClick)
        .background(background)

    Box(contentAlignment = Alignment.Center, modifier = boxModifier) {}
}


@Preview
@Composable
fun EmptyBoardPreview() {
    val board = Board(
        10,
        emptyList(),
        emptyList(),
        emptyList()
    )

    BattleshipMobileTheme {
        BoardView(
            board = board,
            onSquareClicked = { square -> Log.v("BoardView", "Clicked on $square") }
        )
    }
}
