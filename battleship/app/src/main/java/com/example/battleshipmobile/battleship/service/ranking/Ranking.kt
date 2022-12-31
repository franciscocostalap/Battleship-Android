package com.example.battleshipmobile.battleship.service.ranking


data class Statistics(
    val ngames: Int,
    val ranking: List<PlayerStatistics>
) : Ranking()

sealed class Ranking

data class PlayerStatistics(val rank : Int, val playerID : Int, val totalGames: Int, val wins: Int)

data class StatisticsEmbedded(val nGames: Int, val ranking: List<PlayerStatisticsDTO>) : Ranking()

data class PlayerStatisticsDTO(val rank : Int, val player : String, val totalGames: Int, val wins: Int)