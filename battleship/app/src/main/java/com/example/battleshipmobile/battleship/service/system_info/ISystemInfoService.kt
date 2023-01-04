package com.example.battleshipmobile.battleship.service.system_info

import com.example.battleshipmobile.battleship.service.AppService


interface ISystemInfoService : AppService {

    /**
     * Gets the system information
     */
    suspend fun getSysInfo(): SystemInfo

}