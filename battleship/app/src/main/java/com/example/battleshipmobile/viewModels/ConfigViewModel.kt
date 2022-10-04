package com.example.battleshipmobile.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

const val MIN_SHOTS_NUMBER = 0
const val MAX_SHOTS_NUMBER = 10

class ConfigViewModel : ViewModel() {


    private val _nShots = mutableStateOf(1)
    val nShots: Int
        get() = _nShots.value

    fun updateShotNumber(nShots: Int){
        if (nShots <= MIN_SHOTS_NUMBER) {
            return
        }
        if(nShots > MAX_SHOTS_NUMBER){
            return
        }
        _nShots.value = nShots
    }
}