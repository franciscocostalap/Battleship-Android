package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.battleshipmobile.battleship.info.InfoScreenActivity
import com.example.battleshipmobile.battleship.service.user.AuthInfo
import com.example.battleshipmobile.battleship.service.user.AuthInfoDTO
import com.example.battleshipmobile.battleship.service.user.toDTO
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

class HomeActivity: ComponentActivity() {

    companion object{

        private const val AUTH_EXTRA = "AUTH"

        fun navigate(origin: Activity, authInfo: AuthInfo?){
            with(origin){
                val intent = Intent(this, HomeActivity::class.java)
                if (authInfo != null) intent.putExtra(AUTH_EXTRA, authInfo.toDTO())
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                HomeScreen(onClick = {  }) {
                    InfoScreenActivity.navigate(this)
                }

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