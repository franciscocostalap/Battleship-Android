package com.example.battleshipmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.views.screens.HomeScreenActivity
import com.example.battleshipmobile.views.screens.LoginScreenActivity
import com.example.battleshipmobile.views.screens.PlayScreenActivity
import com.example.battleshipmobile.views.screens.info.InfoScreenActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    Column {

                        Button(onClick = { navigateTo(InfoScreenActivity::class) }) {
                            Text(text = "INFO")
                        }

                        Button(onClick = { navigateTo(HomeScreenActivity::class) }) {
                            Text(text = "HOME")
                        }

                        Button(onClick = { navigateTo(LoginScreenActivity::class) }) {
                            Text(text = "LOGIN")
                        }

                        Button(onClick = { navigateTo(PlayScreenActivity::class) }) {
                            Text(text = "PLAY")
                        }

                    }
                }
            }
        }
    }

}