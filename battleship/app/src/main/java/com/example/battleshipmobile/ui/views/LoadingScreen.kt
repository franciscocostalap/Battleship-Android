package com.example.battleshipmobile.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

@Composable
fun LoadingScreen() {
    BattleshipMobileTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.LoadingScreen.Screen),
            color = MaterialTheme.colors.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}