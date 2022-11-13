package com.example.battleshipmobile.battleship.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.LoginForm


@Composable
fun LoginScreen(usernameLabel: Int, passwordLabel: Int, onLoginRequested: (User) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .testTag(TestTags.Login.Screen),
    ) {
        LoginForm(usernameLabel, passwordLabel, onLoginRequested)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    BattleshipMobileTheme {
        LoginScreen(
            R.string.username_field_label,
            R.string.password_field_label
        ){ u ->
            Log.v("LOGIN_PREVIEW", "username: ${u.username} | password: ${u.password}")
        }
    }
}