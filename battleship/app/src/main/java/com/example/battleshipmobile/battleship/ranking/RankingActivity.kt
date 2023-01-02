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
import com.example.battleshipmobile.ui.ErrorAlert
import com.example.battleshipmobile.ui.showToast
import com.example.battleshipmobile.utils.viewModelInit


class RankingActivity: ComponentActivity() {

    companion object{

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
        Log.v("RANKING_ACTIVITY", "RankingActivity onCreate")

        setContent {

            val statisticsResult = rankingViewModel.statisticsResult
            val statistics = rankingViewModel.statistics
            if(statisticsResult != null){
                statisticsResult.onSuccess {
                    Log.v("RANKING_ACTIVITY", "RankingActivity onSuccess")
                    rankingViewModel.statistics = it

                }.onFailure {
                    Log.e("RANKING_ACTIVITY", it.stackTraceToString())
                    ErrorAlert(
                        title = R.string.general_error_title,
                        message = R.string.general_error,
                        buttonText = R.string.ok,
                        onDismiss = { rankingViewModel.clearStatisticsResult() }
                    )
                }
            }else{
                Log.v("RANKING_ACTIVITY", "Fetching statistics")
                rankingViewModel.getStatistics()
            }

            RankingScreen(
                onBackClick = { finish() },
                gameStatistics = statistics,
                onSearchError = {
                    showToast("User $it not found")
                }
            )
        }
    }
}
