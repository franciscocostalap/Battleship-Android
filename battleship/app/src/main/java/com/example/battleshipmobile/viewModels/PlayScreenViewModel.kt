package com.example.battleshipmobile.viewModels

import androidx.lifecycle.ViewModel
import com.example.battleshipmobile.model.Ship


class PlayScreenViewModel() : ViewModel() {

    var _nShotsLeft = 0
    val nShotsLeft : Int
        get() = _nShotsLeft

    //should only be acessible from the api
    fun setShotsLeft(n : Int){
        _nShotsLeft = n
    }

    //
    val _ships : MutableList<Ship> = mutableListOf()
    val ships : List<Ship>
        get() = _ships

    //should only be acessible from the api
    fun updateShips(ships: List<Ship>){
        _ships.clear()
        _ships.addAll(ships)
    }

}