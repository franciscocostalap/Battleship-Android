package com.example.battleshipmobile.battleship.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import com.example.battleshipmobile.service.AuthInfo
import com.example.battleshipmobile.service.AuthInfoDTO
import com.example.battleshipmobile.service.toDTO
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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        Log.v("HOME", "HomeActivity onCreate")
        setContent {
            BattleshipMobileTheme {
                Surface(modifier=Modifier.fillMaxSize()) {
                    val extra = authInfoExtra
                    if(extra != null){
                        Text(text = "Welcome to battleship, user no.${extra.uid}")
                    }else{
                        Text(text="Not Logged in")
                    }
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