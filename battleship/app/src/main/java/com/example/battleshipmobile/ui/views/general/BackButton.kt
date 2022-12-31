package com.example.battleshipmobile.ui.views.general

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.TestTags

@Composable
fun BackButton(
    onBackClick : () -> Unit,
    modifier: Modifier = Modifier,
    padding : Dp = 16.dp
) {
    Button(
        onClick = onBackClick,
        modifier = modifier
            .padding(padding)
            .testTag(TestTags.Statistics.BackButton)
    ) {
        Text(text = stringResource(R.string.back_label))
    }
}