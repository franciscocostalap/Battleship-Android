package com.example.battleshipmobile.views.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.InputTextField
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.R
import com.example.battleshipmobile.TAG
import com.example.battleshipmobile.viewModels.LoginViewModel

class LoginScreenActivity : ComponentActivity() {

    private val vm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, application.javaClass.name)
        setContent {
            BattleshipMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {


                    Column(){

                        Image(
                            painter = painterResource(R.drawable.bs_icon),
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally),

                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = stringResource(R.string.email),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        InputTextField(vm.email.value){ vm.updateEmail(it) }

                        Text(
                            text = stringResource(R.string.password),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        InputTextField(vm.password.value){ vm.updatePassword(it) }

                        //caixas de inpiut
                        Spacer(modifier = Modifier.size(4.dp))

                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = { Log.v(TAG, "Login button pressed with ${vm.email} and ${vm.password}") }
                        ) {
                            Text(text = stringResource(R.string.login))
                        }

                        Button(
                            modifier = Modifier.
                            align(Alignment.CenterHorizontally)
                                .size(100.dp, 30.dp),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(
                                text = stringResource(R.string.register),
                                fontSize =10.sp

                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable()
fun LoginScreen(){
}

