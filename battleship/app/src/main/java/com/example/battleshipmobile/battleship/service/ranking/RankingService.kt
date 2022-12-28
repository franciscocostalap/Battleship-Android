package com.example.battleshipmobile.battleship.service.ranking

import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.RelationType
import com.example.battleshipmobile.battleship.service.buildAndSendRequest
import com.example.battleshipmobile.battleship.service.ensureAction
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
    ) :RankingServiceI {

    private var homeEntity : SirenEntity<Nothing>? = null

    companion object {
        private const val HOME_ERR_MESSAGE = "Home is not set"
        private const val STATISTICS_REL = "statistics"
    }

    private suspend fun fetchHomeEntity() {
        if(homeEntity != null) return

        val request = buildRequest(
            parentURL,
        )
        val responseResult = request.send(client){
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }
        homeEntity = responseResult
    }

    /**
     * Ensures that the ranking url is set
     * Requires that the home entity was fetched first
     *
     */
    private suspend fun ensureStatisticsURL(): Action {
        fetchHomeEntity()

        val homeSirenEntity = homeEntity
        require(homeSirenEntity != null) { HOME_ERR_MESSAGE }

        return ensureAction(
            sirenEntity = homeSirenEntity,
            relation = STATISTICS_REL,
            rootUrl = rootUrl,
            relationType = RelationType.LINK,
            embededInfo = true
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
            action = ensureStatisticsURL(),
        )

        return getEmbeddedStatistics(request)
    }

    /**
     * Extracts the strings between the : and / in the template
     * Could be one or multiple strings
     * Example: the given URI: /api/games/1 and the template: /api/games/:gameID
     * will return the object {gameID: "1"}
     *
     * @param uri The URI to extract the values from.
     * @param template The template to extract the values from.
     * @returns An object with the extracted values.
     */
    private fun extractValues(uri: String, template: String): Map<String, String> {
        val uriParts = uri.split("/")
        val templateParts = template.split("/")

        val values = mutableMapOf<String, String>()

        for (i in uriParts.indices) {
            if (templateParts[i].startsWith(":")) {
                values[templateParts[i].substring(1)] = uriParts[i]
            }
        }

        return values
    }

    /**
     * Returns a [StatisticsEmbedded] object with the embedded players
     * @param sirenStatistics the [SirenEntity<Statistics>] object to get the statistics and the embedded players from
     */
    private fun getEmbeddedStatistics(sirenStatistics : SirenEntity<Statistics>): StatisticsEmbedded {
        val statistics = sirenStatistics.properties ?: throw IllegalStateException("No properties in response")
        val userInfoURI = sirenStatistics.links?.find { it.rel.contains("user") }?.href ?: throw IllegalStateException("No userInfo link in response")


        val embeddedEntities = sirenStatistics.entities
            ?.filterIsInstance<EmbeddedEntity<*>>()
            ?.filter { it.clazz?.get(0)?.equals("user") ?: false } ?: throw IllegalStateException("No embedded entities in response")

        val emb = embeddedEntities.associate { entity ->
            val userInfo = entity.properties as UserInfo
            val embeddedUri = entity.links?.firstOrNull { it.rel.contains("self") }?.href
                ?: throw IllegalStateException("No self link in embedded entity")

            val values = extractValues(embeddedUri.toString(), userInfoURI.toString())
            val id = values["userID"]?.toInt() ?: throw IllegalStateException("No userID in embedded entity")
            id to userInfo
        }

        val ranking = statistics.ranking.map { rankingEntry ->
            val player = emb[rankingEntry.playerID] ?: throw IllegalStateException("No player with id ${rankingEntry.playerID}")

            PlayerStatisticsDTO(
                rank = rankingEntry.rank,
                player = player.name,
                totalGames = rankingEntry.totalGames,
                wins = rankingEntry.wins,
            )
        }
        return StatisticsEmbedded(statistics.nGames,ranking)
    }


}