package com.example.battleshipmobile.battleship.auth

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
import com.example.battleshipmobile.utils.viewModelInit

class AuthenticationActivity : ComponentActivity() {

    companion object {
        fun navigate(origin: Activity) {
            with(origin) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val repo by lazy { dependencies.authInfoRepository }

    private val viewModel by viewModels<AuthViewModel> {
        viewModelInit{ AuthViewModel(dependencies.userService) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("LOGIN_ACTIVITY", "LoginActivity onCreate")
        setContent {

            // Recomposition triggered by this mutable state
            val retrievedAuthResult = viewModel.retrievedAuthInformation
            if(retrievedAuthResult != null){
                retrievedAuthResult.onSuccess {
                    repo.authInfo = it
                    HomeActivity.navigate(this)
                    finish()
                }.onFailure {
                    Log.e("AUTH_ERROR", it.stackTraceToString())
                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = { viewModel.clearAuthResult() }
                    )
                }
            }
            if (repo.authInfo == null)
                AuthenticationScreen(
                    formType=viewModel.authFormType,
                    usernameLabel = R.string.username_field_label,
                    passwordLabel = R.string.password_field_label,
                    onAuthRequested = { user ->
                        val formType = viewModel.authFormType
                        if(formType == AuthenticationFormType.Login)
                            viewModel.login(user)
                        else if(formType == AuthenticationFormType.Register)
                            viewModel.register(user)
                    },
                    onAuthTypeSwapRequested ={
                        viewModel.swapFormType()
                    }
                )
            else error("Unexpected behaviour: authInfo is not null")
        }
    }
}


