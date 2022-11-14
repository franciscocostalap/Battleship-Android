package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.battleshipmobile.battleship.info.InfoScreenActivity
import com.example.battleshipmobile.battleship.play.QueueActivity
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

class HomeActivity: ComponentActivity() {

    companion object{

        private const val AUTH_EXTRA = "AUTH"

        fun navigate(origin: Activity){
            with(origin){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                HomeScreen(onClick = {  }, queueOnClick =  { QueueActivity.navigate(this)}) {
                    InfoScreenActivity.navigate(this)
                }

            }
        }

    }
}