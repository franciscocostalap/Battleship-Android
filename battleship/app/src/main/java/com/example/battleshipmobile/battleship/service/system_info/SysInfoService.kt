package com.example.battleshipmobile.battleship.service.system_info

import com.example.battleshipmobile.battleship.http.buildRequest
import com.example.battleshipmobile.battleship.http.handle
import com.example.battleshipmobile.battleship.http.send
import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.RelationType
import com.example.battleshipmobile.battleship.service.buildAndSendRequest
import com.example.battleshipmobile.battleship.service.ensureAction
import com.example.battleshipmobile.battleship.service.ranking.RankingService
import com.example.battleshipmobile.battleship.service.ranking.Statistics
import com.example.battleshipmobile.utils.SirenEntity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

interface AppService{

    suspend fun fetchParentEntity(client: OkHttpClient,jsonFormatter: Gson,parentURL: URL,parentEntity : SirenEntity<Nothing>?) : SirenEntity<Nothing>? {
        if(parentEntity != null) return parentEntity

        val request = buildRequest(
            parentURL
        )
        val responseResult = request.send(client){
            handle<SirenEntity<Nothing>>(
                SirenEntity.getType<Unit>().type,
                jsonFormatter
            )
        }
        return responseResult
    }
}

class SysInfoService(
    private val client: OkHttpClient,
    private val jsonFormatter: Gson,
    private val rootUrl: String,
    private val parentURL: URL
) : ISystemInfoService {
     var homeEntity: SirenEntity<Nothing>? = null

    companion object{
        private const val HOME_ERR_MESSAGE = "Home is not set"
        private const val INFO_REL = "system-info"
    }

    private suspend fun ensureSysInfoLink(): Action {
        homeEntity = super.fetchParentEntity(client,jsonFormatter,parentURL,homeEntity)

        val homeEntitySiren = homeEntity
        require(homeEntitySiren != null) { HOME_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = homeEntitySiren,
            relation = INFO_REL,
            rootUrl = rootUrl,
            relationType = RelationType.LINK,
            embededInfo = false
        )
    }

    /**
     * Gets the system information
     */
    override suspend fun getSysInfo(): SystemInfo {
        val request = buildAndSendRequest<SystemInfo>(
            client,
            jsonFormatter,
            action =ensureSysInfoLink(),
        )
        return request.properties ?: throw IllegalStateException("No properties in response")
    }

}