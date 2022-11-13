package com.example.battleshipmobile.battleship.play

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.battleship.home.HomeScreen
import com.example.battleshipmobile.battleship.info.InfoScreenActivity
import com.example.battleshipmobile.battleship.play.QueueState.*
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.AuthInfoDTO
import com.example.battleshipmobile.battleship.service.user.toDTO
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.utils.viewModelInit
import java.lang.Thread.sleep


class QueueActivity: ComponentActivity() {

    companion object{
        private const val AUTH_EXTRA = "AUTH"

        fun navigate(origin: Activity, authInfo: AuthInfo?) {
            with(origin){
                val intent = Intent(this, QueueActivity::class.java)
                if (authInfo != null) intent.putExtra(AUTH_EXTRA, authInfo.toDTO())
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
                Log.v("TOKEN", authInfoExtra!!.token)
                if(queueViewModel.lobbyInformation == null)
                    authInfoExtra?.let { queueViewModel.enqueue(it.token) }
                sleep(10000)
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

    @Suppress("deprecation")
    private val authInfoExtra: AuthInfoDTO?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(AUTH_EXTRA, AuthInfoDTO::class.java)
            else
                intent.getParcelableExtra(AUTH_EXTRA)
}