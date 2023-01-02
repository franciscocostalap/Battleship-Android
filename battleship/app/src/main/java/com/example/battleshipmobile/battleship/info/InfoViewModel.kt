package com.example.battleshipmobile.battleship.info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.battleshipmobile.battleship.service.system_info.SysInfoService
import com.example.battleshipmobile.battleship.service.system_info.SystemInfo
import kotlinx.coroutines.launch

class InfoViewModel(private val systemInfoService: SysInfoService) : ViewModel() {
    var sysInfoResult by mutableStateOf<Result<SystemInfo>?>(null)

    var sysInfo by mutableStateOf<SystemInfo?>( null)

    fun getSystemInfo() {
        viewModelScope.launch {
            sysInfoResult = try {
                Result.success(systemInfoService.getSysInfo())
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun clearStatisticsResult() {
        sysInfoResult = null
    }
}