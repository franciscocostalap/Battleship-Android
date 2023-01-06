package com.example.battleshipmobile.battleship.play

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.battleshipmobile.ui.views.game.CustomProgressBar
import kotlinx.coroutines.delay

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

/**
 *  Displays a progress timer bar that counts down from timeout to 0
 *
 *  @param timeout the time in milliseconds to count down from
 *  @param onTimeout the function to call when the timer reaches 0
 */
@Composable
fun ProgressTimer(
    timeout: Long,
    onTimeout: () -> Unit,
    orientation : Orientation = Orientation.HORIZONTAL,

) {
    var progress by rememberSaveable { mutableStateOf(1.0F) }
    val timeoutSeconds = timeout / 1000

    var remainingTime by rememberSaveable { mutableStateOf(timeoutSeconds) }
    var isOver by rememberSaveable { mutableStateOf(false) }


    StatelessProgressBar(
        progress = progress,
        orientation = orientation
    )

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
            progress = newProgress.coerceAtLeast(0F)
        }
    }
}

@Composable
fun StatelessProgressBar(
    progress : Float,
    orientation: Orientation = Orientation.HORIZONTAL
){
    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )
    CustomProgressBar(progress = size, orientation = orientation)
}