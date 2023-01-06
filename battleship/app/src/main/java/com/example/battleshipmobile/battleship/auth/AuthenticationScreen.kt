package com.example.battleshipmobile.battleship.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.service.user.User
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.authType
import com.example.battleshipmobile.ui.dismissKeyboard
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.theme.HEADER_COLOR
import com.example.battleshipmobile.ui.views.auth.AuthenticationForm
import com.example.battleshipmobile.ui.views.home.BattleShipImage

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
    onAuthRequested: (User) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current,
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
                .semantics { this[authType] = formType }
                .testTag(TestTags.Auth.Screen),
            color = MaterialTheme.colors.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BattleShipImage(140.dp)

                Text(
                    text = stringResource(R.string.app_name),
                    color = HEADER_COLOR,
                    fontSize = MaterialTheme.typography.h3.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            dismissKeyboard(focusManager)
                        })
                    },
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
                key(formType) { // Force form full recomposition on form type change
                    AuthenticationForm(
                        usernameLabel = usernameLabel,
                        passwordLabel = passwordLabel,
                        submitLabel = submitLabel,
                        showPasswordStrength = formType == AuthenticationFormType.Register,
                        onSubmitRequested = onAuthRequested,
                        focusManager = focusManager,
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
