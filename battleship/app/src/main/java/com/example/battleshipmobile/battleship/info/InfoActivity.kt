package com.example.battleshipmobile.battleship.info

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.TAG
import com.example.battleshipmobile.ui.ErrorAlert
import com.example.battleshipmobile.utils.viewModelInit


class InfoActivity : ComponentActivity() {


    companion object{
        fun navigate(origin: Activity){
            with(origin){
                val intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        const val ACTIVITY_TAG = "INFO_ACTIVITY"
    }
    private val infoViewModel by viewModels<InfoViewModel> {
        viewModelInit { InfoViewModel(dependencies.systemInfoService) }
    }
    private val dependencies by lazy { application as DependenciesContainer }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "$ACTIVITY_TAG onCreate")
        setContent {
            val sysInfoResult = infoViewModel.sysInfoResult
            val sysInfo = infoViewModel.sysInfo
            if(sysInfoResult != null){
                sysInfoResult.onSuccess {
                    Log.v(ACTIVITY_TAG, "InfoActivity onSuccess")
                    infoViewModel.sysInfo = it
                }.onFailure {
                    Log.e(ACTIVITY_TAG, it.stackTraceToString())
                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = { infoViewModel.clearStatisticsResult() }
                    )
                }
            }else{
                Log.v(ACTIVITY_TAG, "Fetching system info")
                infoViewModel.getSystemInfo()
            }

            InfoScreen(
                onBackClick = { finish() },
                onSendClick = { email -> openSendEmail(email) },
                info = sysInfo,
                isLoading = sysInfo == null
            )
        }
    }

    private fun openSendEmail(email: String){
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                Log.v(ACTIVITY_TAG, "SENDING EMAIL TO: $email")
                putExtra(Intent.EXTRA_EMAIL, listOf(email).toTypedArray())
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
