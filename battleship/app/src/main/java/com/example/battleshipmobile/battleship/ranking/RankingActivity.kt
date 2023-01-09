package com.example.battleshipmobile.battleship.ranking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.battleshipmobile.DependenciesContainer
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.home.HomeActivity
import com.example.battleshipmobile.ui.views.general.ErrorAlert
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.ui.views.LoadingContent
import com.example.battleshipmobile.utils.viewModelInit


class RankingActivity: ComponentActivity() {

    companion object{
        private const val TAG = "RankingActivity"
        fun navigate(origin: Activity){
            with(origin){
                val intent = Intent(this, RankingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private val dependencies by lazy { application as DependenciesContainer }

    private val rankingViewModel by viewModels<RankingViewModel> {
        viewModelInit{ RankingViewModel(dependencies.statisticsService) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "RankingActivity onCreate")

        setContent {

            val statistics = rankingViewModel.statistics
            LoadingContent(isLoading = statistics == null) {
                check(statistics != null)
                RankingScreen(
                    onBackClick = { finish() },
                    gameStatistics = statistics,
                    onSearchError = {
                        showToast(getString(R.string.app_error_user_not_found) + it)
                    }
                )
            }

            val statisticsResult = rankingViewModel.statisticsResult
            if(statisticsResult != null){
                statisticsResult.onSuccess {
                    Log.v(TAG, "RankingActivity onSuccess")
                    rankingViewModel.statistics = it

                }.onFailure {
                    Log.e(TAG, it.stackTraceToString())
                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = {
                            HomeActivity.navigate(this)
                            finish()
                        }
                    )
                }
            }else{
                Log.v(TAG, "Fetching statistics")
                rankingViewModel.getStatistics()
            }

        }
    }
}
