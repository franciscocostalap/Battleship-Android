package com.example.battleshipmobile.battleship.play

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.play.QueueState.*
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit
import java.lang.Thread.sleep


class QueueActivity: ComponentActivity() {

    companion object{
        fun navigate(origin: Activity) {
            with(origin){
                val intent = Intent(this, QueueActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }
    private val queueViewModel by viewModels<QueueViewModel>{
        viewModelInit { QueueViewModel(dependencies.lobbyService) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("QUEUE_ACTIVITY", "QueueActivity onCreate")

        setContent {
            BattleshipMobileTheme {

                val lobbyInformation = queueViewModel.lobbyInformation
                if (lobbyInformation == null) {
                    finish()
                    return@BattleshipMobileTheme
                }

                if(lobbyInformation.gameID == null){
                    QueueScreen(queueState = SEARCHING_OPPONENT)
                    queueViewModel.collectFlow()
                }

                QueueScreen(queueState = FULL)
                sleep(2000)
                //PlaceShipActivity.navigate(this@QueueActivity, ...)
                finish()
            }
        }
    }
}