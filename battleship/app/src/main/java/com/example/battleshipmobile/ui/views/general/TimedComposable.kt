package com.example.battleshipmobile.ui.views.general

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.delay

/**
 * Composable that is only visible for a given amount of time.
 *
 * @param time The time in milliseconds that the composable should be visible.
 * @param content The content of the composable.
 * @param onTimeout The callback that is called when the time is up.
 *
 */
@Composable
fun TimedComposable(time: Long, onTimeout: () -> Unit, content: @Composable () -> Unit) {

    var isOver by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit){
        delay(time)
        isOver = true
        onTimeout()
    }
    if(!isOver){
        content()
    }

}