package com.example.battleshipmobile.battleship.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.ui.ErrorAlert
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit

class LoginActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val repo by lazy { dependencies.authInfoRepository }

    private val loginViewModel by viewModels<LoginViewModel> {
        viewModelInit{ LoginViewModel(dependencies.userService) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("LOGIN_ACTIVITY", "LoginActivity onCreate")
        setContent {
            BattleshipMobileTheme {
                // Update the auth information in the repository before
                // accessing it so it reflects the current state on the recomposition
                //
                // AuthInfo is updated in the ViewModel. Because it is a mutable state,
                // it is recomposed when it changes.

                loginViewModel.authInformation?.onSuccess {
                    repo.authInfo = it
                }?.onFailure {
                    Log.e("LOGIN_ERROR", it.stackTraceToString())

                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = { loginViewModel.logout() }
                    )
                }

                val authInfo = repo.authInfo

                if (authInfo == null)
                    LoginScreen(
                        usernameLabel = R.string.username_field_label,
                        passwordLabel = R.string.password_field_label,
                        onLoginRequested = { user ->
                            loginViewModel.login(user)
                        }
                    )
                else{
                    HomeActivity.navigate(this)
                    finish()
                }
            }
        }
    }
}


