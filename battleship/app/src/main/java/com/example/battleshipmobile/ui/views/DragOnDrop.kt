package com.example.battleshipmobile.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpSize

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

class DragTargetInfo {
    var offsetY = 0f
    var offsetX = 0f
    var inBounds = false
}


@Composable
fun Board(gridCount: Int,gridSize: DpSize) {
    Column {
        for (i in 0..gridCount) {
            Row {
                for (j in 0..gridCount) {
                    DroppableTile(size = gridSize)
                }
            }
        }
    }
}
@Composable
fun DraggableTile(size: DpSize){
    Box(modifier = Modifier
        .size(size).background(Color.Blue)
    )
}


@Composable
fun DroppableTile(size: DpSize){

    Box(modifier = Modifier
        .size(size)
        .onGloballyPositioned {
            println("rect: ${it.boundsInWindow()}")
        }
    )
}





