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
    private val loginViewModel by viewModels<LoginViewModel> {
        viewModelInit{ LoginViewModel(dependencies.userService) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("LOGIN_ACTIVITY", "LoginActivity onCreate")
        setContent {
            BattleshipMobileTheme {

                val authInfo = loginViewModel.authInformation

                if (authInfo == null || loginViewModel.authInformation?.isFailure == true) {
                    LoginScreen { username, password ->
                        loginViewModel.login(username, password)
                    }
                }

                authInfo?.onSuccess {
                    HomeActivity.navigate(this@LoginActivity, it)
                    finish()
                }?.onFailure {
                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.confirm,
                        onDismiss = { finishAndRemoveTask() }
                    )
                }
            }
        }
    }

}
