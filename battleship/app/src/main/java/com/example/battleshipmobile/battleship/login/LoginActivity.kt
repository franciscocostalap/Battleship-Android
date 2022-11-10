package com.example.battleshipmobile.battleship.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.service.User
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

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
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(dependencies.userService) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (loginViewModel.authInformation == null) {
                        LoginScreen { username, password ->
                            loginViewModel.login(User(username, password))
                        }
                    } else {
                        HomeActivity.navigate(this, loginViewModel.authInformation)
                    }
                }
            }
        }
    }

}
