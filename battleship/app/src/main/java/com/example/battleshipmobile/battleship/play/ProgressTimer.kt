package com.example.battleshipmobile.battleship.play

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.example.battleshipmobile.ui.theme.Blue200
import com.example.battleshipmobile.ui.theme.Blue700
import com.example.battleshipmobile.ui.views.game.CustomProgressBar
import kotlinx.coroutines.delay

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

@Composable
fun TimerLogic(
    timeToDefineLayout: Long,
    onProgressChange: (progress: Float) -> Unit,
    onTimeout: () -> Unit,
){
    val timeoutSeconds = timeToDefineLayout / 1000

    var remainingTime by rememberSaveable { mutableStateOf(timeoutSeconds) }
    var isOver by rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(key1 = true){
        while(!isOver) {
            if (remainingTime == 0L) {
                onTimeout()
                isOver = true
            }
            delay(1000)
            val updatedTime = remainingTime - 1
            remainingTime = updatedTime.coerceAtLeast(0L)

            val newProgress = remainingTime / timeoutSeconds.toFloat()
            onProgressChange(newProgress.coerceAtLeast(0F))
        }
    }
}

data class BarColors(
    val background: Color,
    val foreground: Color
)

@Composable
fun StatelessProgressBar(
    progress : Float,
    orientation: Orientation = Orientation.HORIZONTAL,
    barColor: BarColors = BarColors(Blue200, Blue700),
){
    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )
    CustomProgressBar(progress = size, orientation = orientation, barColor = barColor)
}