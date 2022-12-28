package com.example.battleshipmobile.ui.views.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.Password
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.Username
import com.example.battleshipmobile.battleship.service.user.validate
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.dismissKeyboard
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.OutlinedTextFieldValidation
import com.example.battleshipmobile.ui.views.PasswordField


private val usernameErrorLabels = mapOf(
    Username.Validation.EMPTY to R.string.field_is_blank,
    Username.Validation.TOO_SHORT to R.string.username_is_too_short
)

private val passwordErrorLabels = mapOf(
    Password.Validation.EMPTY to R.string.field_is_blank,
    Password.Validation.TOO_SHORT to R.string.password_is_too_short,
)

/**
 * Defines which
 */
data class IgnoredValidation(
    val username: Set<Username.Validation> = emptySet(),
    val password: Set<Password.Validation> = emptySet()
)

/**
 * Composable that displays an Authentication form.
 *
 * Username and password field, and a submit button.
 *
 * @param onSubmitRequested Callback that is called with a [User] object defined as parameters
 * when the user clicks the submit button. It is where the validation is made.
 *
 * @param ignoredValidation Defines which validations you may want to ignore for username or password.
 *
 */
@Composable
fun AuthenticationForm(
    @StringRes usernameLabel: Int,
    @StringRes passwordLabel: Int,
    @StringRes submitLabel: Int,
    showPasswordStrength: Boolean = false,
    onSubmitRequested: (User) -> Unit,
    focusManager: FocusManager,
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isUsernameError by rememberSaveable { mutableStateOf(false) }
    var isPasswordError by rememberSaveable { mutableStateOf(false) }
    var userErrorMessage: Int? by rememberSaveable { mutableStateOf(null) }
    var passwordErrorMessage: Int? by rememberSaveable { mutableStateOf(null) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    val usernameErrorLabel = userErrorMessage?.let { stringResource(it) } ?: ""
    val passwordErrorLabel = passwordErrorMessage?.let { stringResource(it) } ?: ""


    StatelessAuthenticationForm(
        username = username,
        password = password,
        usernameLabel = stringResource(id = usernameLabel),
        passwordLabel = stringResource(id = passwordLabel),
        submitLabel = stringResource(id = submitLabel),
        isPasswordVisible = passwordVisible,
        onUserNameChange = { username = it; isUsernameError = false },
        onPasswordChange = { password = it; isPasswordError = false },
        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
        showPasswordStrength = showPasswordStrength,
        focusManager = focusManager,
        onSubmitRequest = submit@{ uname, pwd ->

            val usernameValidation = Username.validate(
                value=uname.trim(),
            )

            val passwordValidation = Password.validate(
                value=pwd,
            )

            isUsernameError = usernameValidation.isNotEmpty()
            isPasswordError = passwordValidation.isNotEmpty()

            if (!isUsernameError && !isPasswordError) {
                onSubmitRequested(User(uname, pwd))
                return@submit
            }

            if(isUsernameError) {
                userErrorMessage = usernameErrorLabels[usernameValidation.first()]
            }

            if(isPasswordError) {
                passwordErrorMessage = passwordErrorLabels[passwordValidation.first()]
            }
        },
        isUsernameError = isUsernameError,
        isPasswordError = isPasswordError,
        usernameErrorLabel = usernameErrorLabel,
        passwordErrorLabel = passwordErrorLabel
    )
}

/**
 * Used as an implementation detail of [AuthenticationForm].
 */
@Composable
private fun StatelessAuthenticationForm(
    username: String,
    password: String,
    usernameLabel: String,
    passwordLabel: String,
    submitLabel: String,
    showPasswordStrength: Boolean = false,
    isPasswordVisible: Boolean,
    isUsernameError: Boolean = false,
    isPasswordError: Boolean = false,
    usernameErrorLabel: String = "",
    passwordErrorLabel: String = "",
    onUserNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmitRequest: (String, String) -> Unit,
    focusManager: FocusManager
) {
    Column(
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
            modifier = Modifier.testTag(TestTags.Auth.UsernameField),
            error=usernameErrorLabel,
            keyboardActions = KeyboardActions(
                onDone = { dismissKeyboard(focusManager) }
            )
        )
        PasswordField(
            value = password,
            isVisible = isPasswordVisible,
            isError = isPasswordError,
            onValueChange = onPasswordChange,
            onVisibilityToggle = onPasswordVisibilityToggle,
            label = passwordLabel,
            modifier = Modifier.testTag(TestTags.Auth.PasswordField),
            error = passwordErrorLabel,
            keyboardActions = KeyboardActions(
                onDone = { dismissKeyboard(focusManager) }
            )
        )
        if (showPasswordStrength) {
            PasswordStrengthIndicator(password)
        }
        Button(
            onClick = {
                onSubmitRequest(username, password)
            },
            modifier = Modifier.testTag(TestTags.Auth.SubmitButton),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = submitLabel)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun AuthFormWithErrorsPreview() {
    BattleshipMobileTheme {
        StatelessAuthenticationForm(
            username = "costakilapada",
            password = "craques",
            usernameLabel = "Username",
            passwordLabel = "Password",
            submitLabel = "Sign In",
            isPasswordVisible = false,
            isUsernameError = true,
            isPasswordError = true,
            usernameErrorLabel = "Username is blank",
            passwordErrorLabel = "Password must have at least 8 characters",
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { _, _ -> },
            focusManager = LocalFocusManager.current
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthFormRegisterPreview() {
    BattleshipMobileTheme {
        StatelessAuthenticationForm(
            username = "costakilapada",
            password = "craques",
            usernameLabel = "Username",
            passwordLabel = "Password",
            submitLabel = "Sign Up",
            isPasswordVisible = false,
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { _, _ -> },
            focusManager = LocalFocusManager.current
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthFormVisiblePasswordWithStrenghtPreview() {
    BattleshipMobileTheme {
        StatelessAuthenticationForm(
            username = "costakilapada",
            password = "craquesdabola123D!",
            usernameLabel = "Username",
            passwordLabel = "Password",
            submitLabel = "Sign Up",
            showPasswordStrength = true,
            isPasswordVisible = true,
            onUserNameChange = {},
            onPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onSubmitRequest = { _, _ -> },
            focusManager = LocalFocusManager.current
        )
    }
}