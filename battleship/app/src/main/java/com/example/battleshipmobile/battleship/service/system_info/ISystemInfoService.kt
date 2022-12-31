package com.example.battleshipmobile.battleship.service.system_info


interface ISystemInfoService : AppService {

    /**
     * Gets the system information
     */
    suspend fun getSysInfo(): SystemInfo

}