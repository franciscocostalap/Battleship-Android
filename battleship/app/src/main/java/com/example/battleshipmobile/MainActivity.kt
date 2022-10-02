package com.example.battleshipmobile


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.views.screens.HomeScreen
import com.example.battleshipmobile.views.screens.info.InfoActivity

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
                    HomeScreen(onClick = {  }) {
                        navigateToInfoScreen()
                    }
                }
            }
        }
    }

    private fun navigateToInfoScreen() {
        val intent = Intent(this, InfoActivity::class.java)
        startActivity(intent)
    }
}



