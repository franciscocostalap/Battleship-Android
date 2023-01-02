package com.example.battleshipmobile.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.focus.FocusManager

/**
 * Shows a toast message in [this] context.
 *
 * @param msg The message to show.
 * @param duration The duration of the toast. Either [Toast.LENGTH_SHORT] or [Toast.LENGTH_LONG]
 */
fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        this,
        msg,
        duration
    ).show()
}

/**
 * Hides the keyboard given the [FocusManager]
 */
fun dismissKeyboard(focusManager: FocusManager) {
    focusManager.clearFocus()
}

fun LazyListState.isItemVisible(index: Int): Boolean
        = firstVisibleItemIndex <= index && index < firstVisibleItemIndex + this.layoutInfo.visibleItemsInfo.size
