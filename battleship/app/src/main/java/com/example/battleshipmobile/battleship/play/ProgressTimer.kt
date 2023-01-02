package com.example.battleshipmobile.battleship.play

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import com.example.battleshipmobile.battleship.components.CustomProgressBar
import kotlinx.coroutines.delay

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
) {
    var progress by remember { mutableStateOf(1.0F) }
    val timeoutSeconds = timeout / 1000
    var remainingTime by remember { mutableStateOf(timeoutSeconds) }
    var isOver by remember { mutableStateOf(false) }

    val size by animateFloatAsState(
        targetValue = progress,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        )
    )

    CustomProgressBar(progress = size)

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