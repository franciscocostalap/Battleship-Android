package com.example.battleshipmobile.ui

import android.content.Context
import android.widget.Toast

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