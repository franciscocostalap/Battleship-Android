package com.example.battleshipmobile.views


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun CenterAligned(horizontal: Alignment.Horizontal = Alignment.CenterHorizontally, vertical: Arrangement.Vertical = Arrangement.Center, content: @Composable() (ColumnScope.() -> Unit)){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = horizontal,
        verticalArrangement = vertical) {
        content
    }
}