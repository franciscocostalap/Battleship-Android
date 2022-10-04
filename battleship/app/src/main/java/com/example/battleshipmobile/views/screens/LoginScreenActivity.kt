package com.example.battleshipmobile.views.screens

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.*
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.viewModels.LoginViewModel




@Composable
fun InputTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}

class LoginScreenActivity : ComponentActivity() {

    private val vm by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BattleshipMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {





                    Column(){
                        Button(onClick = { navigateTo(ConfigScreenActivity::class) }) {
                            Text(text = "navegar")
                        }
                        Image(
                            painter = painterResource(R.drawable.bs_icon),
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.CenterHorizontally),

                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = stringResource(R.string.username),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        InputTextField(vm.name.value){ vm.updateName(it) }

                        Text(
                            text = stringResource(R.string.password),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        InputTextField(vm.password.value){ vm.updatePassword(it) }


                        Spacer(modifier = Modifier.size(4.dp))

                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                FakeApi().login(vm.name.value.text, vm.password.value.text)

                                Log.v(TAG, "Login button pressed with ${vm.name} and ${vm.password}")
                            }
                        ) {
                            Text(text = stringResource(R.string.login))
                        }

                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(100.dp, 30.dp),
                            onClick = {

                                if(FakeApi().register(vm.name.value.text, vm.password.value.text)){
                                 //   navigateTo(HomeScreenActivity::class)
                                }
                                //se correr trd bem navegar pa escra home
                            }
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

