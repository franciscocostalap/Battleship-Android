package com.example.battleshipmobile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import kotlin.math.roundToInt


val GRID_SIZE = 50.dp

@Composable
fun Grid() {
    Box(modifier = Modifier
        .size(GRID_SIZE)
        .border(1.dp, Color.Black)
    )
}

@Composable
fun Ship(size : Int = 1) {
//    Box(modifier = Modifier
//        .size(25.dp)
//        .background(Blue)
//    )
    Box(modifier = Modifier
        .width(GRID_SIZE * size)
        .height(GRID_SIZE)) {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }

        Box(
            Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .background(Color.Blue)
                .width(GRID_SIZE * size)
                .height(GRID_SIZE)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}


@Composable
fun Board( grids : Int){
    Column {
        for (i in 0..grids) {
            Row {
                for (j in 0..grids) {
                    Grid()
                }
            }
        }
    }
}

@Composable
fun gameScreen(){
    BattleshipMobileTheme {
        Row {
            Spacer(modifier = Modifier.size(12.dp))

            Column() {
                Spacer(modifier = Modifier.size(12.dp))
                Board(6)
                Spacer(modifier = Modifier.size(12.dp))
                Row {
                    Ship(2)
                    Spacer(modifier = Modifier.size(2.dp))
                    Ship(3)
                    Spacer(modifier = Modifier.size(2.dp))
                    Ship()
                    Spacer(modifier = Modifier.size(2.dp))
                    Ship()
                    Spacer(modifier = Modifier.size(2.dp))
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }

    }
}
