package com.example.battleshipmobile.battleship.login

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R


@Composable
fun PasswordField(
    value: String,
    isVisible: Boolean,
    onValueChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit
) {
    val passwordFieldText = stringResource(id = R.string.password)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(passwordFieldText) },
        singleLine = true,
        placeholder = { Text(passwordFieldText) },
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (isVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (isVisible) "Hide password" else "Show password"

            IconButton(onClick = onVisibilityToggle) {
                Icon(imageVector = image, description)
            }
        },
    )

}


@Composable
fun LoginScreen(onLoginRequested: (String, String) -> Unit) {

    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    StatelessLoginScreen(
        username = username,
        password = password,
        isPasswordVisible = passwordVisible,
        onUserNameChange = { username = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        onSubmitRequest = onLoginRequested
    )

}

@Composable
fun StatelessLoginScreen(
    username: String,
    password: String,
    isPasswordVisible: Boolean,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmitRequest: (String, String) -> Unit
) {
    val usernameFieldText = stringResource(id = R.string.username)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = onUserNameChange,
            label = { Text(usernameFieldText) },
            singleLine = true,
            placeholder = { Text(usernameFieldText) }
        )
        Spacer(modifier = Modifier.height(10.dp))
        PasswordField(
            value = password,
            isVisible = isPasswordVisible,
            onValueChange = onPasswordChange,
            onVisibilityToggle = onPasswordVisibilityToggle
        )

        Button(onClick = { onSubmitRequest(username, password) }) {
            Text(text = stringResource(id = R.string.login))
        }
    }

}

@Preview
@Composable
fun LoginScreenFilledPreview() {

    BattleshipMobileTheme {
        StatelessLoginScreen(
            username = "costakilapada",
            password = "craques",
            isPasswordVisible = false,
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { s, s1 -> }
        )
    }

}

@Preview
@Composable
fun LoginScreenPreview() {
    BattleshipMobileTheme {
        LoginScreen { u, p ->
            Log.v("LOGIN_PREVIEW", "username: $u | password: $p")
        }
    }
}