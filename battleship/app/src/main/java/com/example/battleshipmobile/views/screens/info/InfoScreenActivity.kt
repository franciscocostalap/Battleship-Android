package com.example.battleshipmobile.views.screens.info

import Credits
import SocialInfo
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.battleshipmobile.TAG



class InfoScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Credits(
                onSendEmailRequested = { openSendEmail() },
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

val socialsPreview = listOf(
    SocialInfo(
        name = "Francisco Costa",
        nr = 48282,
        email =  "a48282@alunos.isel.pt"
    ),
    SocialInfo(
        name = "Tiago Filipe",
        nr = 48265,
        email = "a48265@alunos.isel.pt"
    ),
    SocialInfo(
        name = "Teodosie Pienescu",
        nr = 48267,
        email = "a48267@alunos.isel.pt"
    )
)

private const val emailSubject = "About the BattleShip game"
