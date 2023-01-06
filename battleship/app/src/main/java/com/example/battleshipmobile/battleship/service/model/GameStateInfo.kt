package com.example.battleshipmobile.battleship.service.model

import com.example.battleshipmobile.battleship.service.ID


data class GameStateInfo(
    val state: State,
    val turnID: ID,
    val player1ID: ID,
    val player2ID: ID,
    val remainingTime: Long
)

enum class State { PLACING_SHIPS, PLAYING, FINISHED, CANCELLED }