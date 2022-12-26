package com.example.battleshipmobile.battleship.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.Password
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.battleship.service.user.Username
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.authType
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.AuthenticationForm
import com.example.battleshipmobile.ui.views.IgnoredValidation

enum class AuthenticationFormType{
    Login,
    Register
}

@Composable
fun AuthenticationScreen(
    formType: AuthenticationFormType,
    @StringRes usernameLabel: Int,
    @StringRes passwordLabel: Int,
    onAuthTypeSwapRequested: () -> Unit,
    onAuthRequested: (User) -> Unit
) {
    BattleshipMobileTheme {
        val accountText = when(formType){
            AuthenticationFormType.Login -> R.string.dont_have_an_account
            AuthenticationFormType.Register -> R.string.already_have_an_account
        }
        val submitLabel = when(formType){
            AuthenticationFormType.Login -> R.string.sign_in
            AuthenticationFormType.Register -> R.string.sign_up
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .semantics { this[authType] = formType }
                .testTag(TestTags.Auth.Screen),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(accountText),
                    style = TextStyle(
                        color = Color(0xFF1067FF),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.SansSerif,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier= Modifier
                        .clickable { onAuthTypeSwapRequested() }
                        .testTag(TestTags.Auth.AuthSwapLink),
                )
                key(formType) { // Force form full recreation on form type change
                    AuthenticationForm(
                        usernameLabel = usernameLabel,
                        passwordLabel = passwordLabel,
                        submitLabel = submitLabel,
                        onSubmitRequested = onAuthRequested
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    AuthenticationScreen(
        formType = AuthenticationFormType.Register,
        usernameLabel = R.string.username_field_label,
        passwordLabel = R.string.password_field_label,
        onAuthTypeSwapRequested = { },
        onAuthRequested = { }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    AuthenticationScreen(
        formType = AuthenticationFormType.Login,
        usernameLabel = R.string.username_field_label,
        passwordLabel = R.string.password_field_label,
        onAuthTypeSwapRequested = { },
        onAuthRequested = { }
    )
}
