package com.example.battleshipmobile.ui.views

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.*

/**
 * A Composable [content] will be displayed for a given limited amount of time [timeToWait] and then
 * will perform an action [onTimeout]
 *
 * @param timeToWait the time in milliseconds that the [Composable] will be displayed
 * @param onTimeout the action to be performed when the time is over
 * @param content the [Composable] to be displayed
 */
@Composable
fun TimedComposable(
    timeToWait: Long,
    onTimeout: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val state = rememberSaveable { mutableStateOf(true) }
    var initialMoment by rememberSaveable { mutableStateOf<Long?>(null) }
    var remainingTime by rememberSaveable { mutableStateOf(timeToWait) }


    LaunchedEffect(key1 = true) {
        if(initialMoment == null) {
            initialMoment = System.currentTimeMillis()
        }
        coroutineContext.job.invokeOnCompletion {
            val initMoment = initialMoment
            if(it is  CancellationException && initMoment != null) {
                val currentTime = System.currentTimeMillis()
                val timeElapsed = currentTime - initMoment
                remainingTime = (timeToWait - timeElapsed).coerceAtLeast(0)
            }
        }

        delay(remainingTime)
        state.value = false
        onTimeout()
    }

    if (state.value) {
        content()
    }
}