package com.example.battleshipmobile.battleship.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.ranking.StatisticsEmbedded
import com.example.battleshipmobile.battleship.service.ranking.RankingServiceI
import kotlinx.coroutines.launch

class RankingViewModel(private val userService: RankingServiceI) : ViewModel() {

    var statisticsResult by mutableStateOf<Result<StatisticsEmbedded>?>(null)
    private set

    var statistics by mutableStateOf<StatisticsEmbedded?>(null)


    fun getStatistics() {
        viewModelScope.launch {
            statisticsResult = try {
                Result.success(userService.getStatistics(true))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}