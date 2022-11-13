package com.example.battleshipmobile.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.*
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme


private val usernameErrorLabels = mapOf(
    UsernameValidation.EMPTY to R.string.field_is_blank,
    UsernameValidation.TOO_SHORT to R.string.username_is_too_short
)

private val passwordErrorLabels = mapOf(
    PasswordValidation.EMPTY to R.string.field_is_blank,
    PasswordValidation.TOO_SHORT to R.string.password_is_too_short,
    PasswordValidation.NO_DIGITS to R.string.password_must_have_a_digit,
    PasswordValidation.NO_SPECIAL_CHARACTERS to R.string.password_must_have_a_special_character
)

/**
 * Composable that displays a login form.
 *
 * Has a username and password field, and a submit button.
 *
 * @param onLoginRequested Callback that is called with the parameters (username, password)
 * when the user clicks the submit button.
 *
 */
@Composable
fun LoginForm(usernameLabel: Int, passwordLabel: Int, onLoginRequested: (User) -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var userError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var userErrorMessage: Int? by rememberSaveable { mutableStateOf(null) }
    var passwordErrorMessage: Int? by rememberSaveable { mutableStateOf(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val usernameErrorLabel = userErrorMessage?.let { stringResource(it) } ?: ""
    val passwordErrorLabel = passwordErrorMessage?.let { stringResource(it) } ?: ""


    StatelessLoginForm(
        username = username,
        password = password,
        usernameLabel = stringResource(id = usernameLabel),
        passwordLabel = stringResource(id = passwordLabel),
        isPasswordVisible = passwordVisible,
        onUserNameChange = { username = it },
        onPasswordChange = { password = it },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        onSubmitRequest = { uname, pwd ->
            val validationResult = validateUserInfo(uname, pwd)
            if (validationResult == User.validValidationPair) {
                onLoginRequested(User(uname, pwd))
            } else {
                val (usernameValidation, passwordValidation) = validationResult
                userError = usernameValidation != UsernameValidation.VALID
                passwordError = passwordValidation != PasswordValidation.VALID

                userErrorMessage = usernameErrorLabels[usernameValidation]
                passwordErrorMessage = passwordErrorLabels[passwordValidation]
            }
        },
        isUsernameError = userError,
        isPasswordError = passwordError,
        usernameErrorLabel = usernameErrorLabel,
        passwordErrorLabel = passwordErrorLabel
    )
}

/**
 * Used as an implementation detail of [LoginForm].
 */
@Composable
private fun StatelessLoginForm(
    username: String,
    password: String,
    usernameLabel: String,
    passwordLabel: String,
    isPasswordVisible: Boolean,
    isUsernameError: Boolean = false,
    isPasswordError: Boolean = false,
    usernameErrorLabel: String,
    passwordErrorLabel: String,
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmitRequest: (String, String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextFieldValidation(
            value = username,
            onValueChange = onUserNameChange,
            isError = isUsernameError,
            label = { Text(usernameLabel) },
            singleLine = true,
            placeholder = { Text(usernameLabel) },
            modifier = Modifier.testTag(TestTags.Login.UsernameField),
            error=usernameErrorLabel
        )
        Spacer(modifier = Modifier.height(10.dp))
        PasswordField(
            value = password,
            isVisible = isPasswordVisible,
            isError = isPasswordError,
            onValueChange = onPasswordChange,
            onVisibilityToggle = onPasswordVisibilityToggle,
            label = passwordLabel,
            modifier = Modifier.testTag(TestTags.Login.PasswordField),
            error = passwordErrorLabel
        )
        Button(
            onClick = {
                onSubmitRequest(username, password)
            },
            modifier = Modifier.testTag(TestTags.Login.SubmitButton)
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }

}

@Preview
@Composable
private fun LoginScreenFilledPreview() {
    BattleshipMobileTheme {
        StatelessLoginForm(
            username = "costakilapada",
            password = "craques",
            usernameLabel = "Username",
            passwordLabel = "Password",
            isPasswordVisible = false,
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { s, s1 -> },
            isUsernameError = false,
            isPasswordError = false,
            usernameErrorLabel = "",
            passwordErrorLabel = ""
        )
    }
}

@Preview
@Composable
private fun LoginScreenWithErrorsPreview() {
    BattleshipMobileTheme {
        StatelessLoginForm(
            username = "costakilapada",
            password = "craques",
            usernameLabel = "Username",
            passwordLabel = "Password",
            isPasswordVisible = false,
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { s, s1 -> },
            isUsernameError = true,
            isPasswordError = true,
            usernameErrorLabel = "Username is blank",
            passwordErrorLabel = "Password must have a special character"
        )
    }
}