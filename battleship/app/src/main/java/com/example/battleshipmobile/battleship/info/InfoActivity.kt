package com.example.battleshipmobile.battleship.info

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.battleshipmobile.TAG



class InfoActivity : ComponentActivity() {


    companion object{
        fun navigate(origin: Activity){
            with(origin){
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InfoScreen(
                socials = socialsPreview
            )
        }
    }

    private fun openSendEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, socialsPreview.map { it.email }.toTypedArray())
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            }
            startActivity(intent)
        }
        catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Failed to send email", e)
        }
    }
}

private const val emailSubject = "About the BattleShip game"
