package com.example.battleshipmobile.ui.views.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.battleshipmobile.battleship.service.model.Orientation

@Composable
fun OrientedLayout(
    modifier: Modifier = Modifier,
    orientation: Orientation,
    content: @Composable () -> Unit
) {
    when (orientation) {
        Orientation.Horizontal -> Row(modifier = modifier) {
            content()
        }
        Orientation.Vertical -> Column(modifier = modifier) {
            content()
        }
    }
}