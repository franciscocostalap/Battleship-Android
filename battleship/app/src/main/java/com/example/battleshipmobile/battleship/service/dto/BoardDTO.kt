package com.example.battleshipmobile.battleship.service.dto

import com.example.battleshipmobile.battleship.service.UserID
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.Square

/**
 * A Board representation containing all the relevant information to the client
 */
data class BoardDTO(
    val userID: UserID,
    val shipParts: List<Square>,
    val shots: List<Square>,
    val hits: List<Square>,
    val side: Int,
)


/**
 * Converts DTO to model
 */
fun BoardDTO.toBoard() = Board(
    shipParts = shipParts,
    shots = shots,
    hits = hits,
    side = side,
)