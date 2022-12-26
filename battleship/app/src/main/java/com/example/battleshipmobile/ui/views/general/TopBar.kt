package com.example.battleshipmobile.ui.views.general

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.battleshipmobile.R


data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null
)

@Composable
fun TopBar(navigationHandlers: NavigationHandlers) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            if (navigationHandlers.onBackRequested != null)
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable { navigationHandlers.onBackRequested.invoke() }
                )
        }
    )
}