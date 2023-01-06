package com.example.battleshipmobile.utils

import kotlinx.coroutines.channels.ProducerScope

/**
 * Executes the given [block] closing the [ProducerScope] in case of an exception.
 */
suspend fun <T> ProducerScope<T>.use(block: suspend ProducerScope<T>.() -> Unit) {
    try {
        block()
    }catch (e: Exception) {
        close(e)
    } finally {
        check(!close()){
            "Channel wasn't properly closed"
        }
    }
}