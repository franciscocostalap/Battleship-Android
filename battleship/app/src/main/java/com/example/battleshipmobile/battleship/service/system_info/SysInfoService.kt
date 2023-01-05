package com.example.battleshipmobile.battleship.service.system_info

import com.example.battleshipmobile.battleship.service.Action
import com.example.battleshipmobile.battleship.service.RelationType
import com.example.battleshipmobile.battleship.service.buildAndSendRequest
import com.example.battleshipmobile.battleship.service.ensureAction
import com.example.battleshipmobile.utils.SirenEntity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import java.net.URL

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

    /**
     * Ensures that the SystemInfo Action is present in the home entity
     * Requires that the home was fetched first.
     *
     * @return [Action] the action or link url and method
     */
    private suspend fun ensureSysInfoLink(): Action {
        homeEntity = super.fetchParentEntity(client,jsonFormatter,parentURL,homeEntity)

        val homeEntitySiren = homeEntity
        require(homeEntitySiren != null) { HOME_ERR_MESSAGE }

        return ensureAction(
            parentSirenEntity = homeEntitySiren,
            relation = INFO_REL,
            rootUrl = rootUrl,
            relationType = RelationType.LINK,
            embeddedInfo = false
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