package com.example.battleshipmobile.battleship.service.ranking


import com.example.battleshipmobile.battleship.service.*
import com.example.battleshipmobile.battleship.service.dto.OutputUserDTO
import com.example.battleshipmobile.battleship.service.user.UserInfo
import com.example.battleshipmobile.utils.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

class RankingService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentURL: URL
    ) : RankingServiceI {

    private var homeEntity : SirenEntity<Nothing>? = null

    companion object {
        private const val HOME_ERR_MESSAGE = "Home is not set"
        private const val STATISTICS_REL = "statistics"
    }

    /**
     * Ensures that the ranking url is set
     * Requires that the home entity was fetched first
     *
     */
    private suspend fun ensureStatisticsURL(): Relation {
        homeEntity = super.fetchParentEntity(client, jsonFormatter, parentURL,homeEntity)

        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureRelation(
            parentSirenEntity = homeSirenEntity,
            relation = STATISTICS_REL,
            rootUrl = rootUrl,
            relationType = RelationType.LINK,
            embeddedInfo = true
        )
    }

    /**
     * Makes a request to the server to get the statistics of the game
     * @return the [Statistics] provided by the server
     */
    override suspend fun getStatistics(embeddedPlayers : Boolean): StatisticsEmbedded {
        val request = buildAndSendRequest<Statistics>(
            client,
            jsonFormatter,
            relation = ensureStatisticsURL(),
        )

        return getEmbeddedStatistics(request)
    }


    /**
     * Returns a [StatisticsEmbedded] object with the embedded players
     * @param sirenStatistics the [SirenEntity<Statistics>] object to get the statistics and the embedded players from
     */
    private fun getEmbeddedStatistics(sirenStatistics : SirenEntity<Statistics>): StatisticsEmbedded {
        val statistics = sirenStatistics.properties ?: throw IllegalStateException("No properties in response")
        val userInfoRelation = "user"
        val userInfoURI = sirenStatistics.links?.find { it.rel.contains(userInfoRelation) }?.href ?: throw IllegalStateException("No userInfo link in response")

        val embeddedEntities = sirenStatistics.filterEmbeddedEntitiesFor(userInfoRelation)

        val embeddedInfo = embeddedEntities.associate { entity ->
            val embeddedUri = entity.links?.firstOrNull { it.rel.contains("self") }?.href
                ?: throw IllegalStateException("No self link in embedded entity")

            val values = extractValues(embeddedUri.toString(), userInfoURI.toString())["userID"]?.toInt()
            val id = values ?: throw IllegalStateException("No userID in embedded entity")
            id to entity.properties as OutputUserDTO
        }

        val ranking = statistics.ranking.map { rankingEntry ->
            val player = embeddedInfo[rankingEntry.playerID] ?: throw IllegalStateException("No player with id ${rankingEntry.playerID}")

            PlayerStatisticsDTO(
                rank = rankingEntry.rank,
                player = player.name,
                totalGames = rankingEntry.totalGames,
                wins = rankingEntry.wins,
            )
        }
        return StatisticsEmbedded(statistics.ngames,ranking)
    }


}