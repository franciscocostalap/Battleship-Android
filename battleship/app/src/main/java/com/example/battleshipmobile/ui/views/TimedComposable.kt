package com.example.battleshipmobile.ui.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

/**
 * A Composable [content] will be displayed for a given limited amount of time [time] and then
 * will perform an action [onTimeout]
 *
 * @param time the time in milliseconds that the [Composable] will be displayed
 * @param onTimeout the action to be performed when the time is over
 * @param content the [Composable] to be displayed
 */
@Composable
fun TimedComposable(
    time: Long,
    onTimeout: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val state = remember { mutableStateOf(true) }
    LaunchedEffect(key1 = true) {
        delay(time)
        state.value = false
        onTimeout()
    }

    if (state.value) {
        content()
    }
}