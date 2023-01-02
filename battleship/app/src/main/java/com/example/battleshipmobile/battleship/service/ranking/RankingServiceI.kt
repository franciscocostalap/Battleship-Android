package com.example.battleshipmobile.battleship.service.ranking

import com.example.battleshipmobile.battleship.service.system_info.AppService

interface RankingServiceI : AppService {

    /**
     * Makes a request to the server to get the statistics of the game
     * @return the [Statistics] provided by the server
     */
    suspend fun getStatistics(embeddedPlayers : Boolean = false): StatisticsEmbedded
}
