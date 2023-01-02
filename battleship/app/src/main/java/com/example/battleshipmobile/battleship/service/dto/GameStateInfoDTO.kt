package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.ID
import com.example.battleshipmobile.battleship.service.model.State


data class GameStateInfoDTO(val state: State, val turnID: ID, val player1ID: ID, val player2ID: ID)