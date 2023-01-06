package com.example.battleshipmobile.battleship.play.shotDefinition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.play.ProgressTimer
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.Square
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.game.BoardView

data class GameBoards(
    val myBoard: Board,
    val opponentBoard: Board
)

enum class GameTurn {
    MY,
    OPPONENT
}

data class ShotsDefinitionHandlers(
    val onOpponentBoardSquareClicked: (Square) -> Unit = {},
    val onSubmitShotsClick: () -> Unit = {},
    val onTimeout: () -> Unit = {}
)

data class ShotsDefinitionScreenState(
    val boards: GameBoards,
    val turn: GameTurn,
    val remainingTime: Long,
    val timerResetToggle: Boolean,
)

@Composable
fun ShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    timeToDefineShot: Long
) {
    BattleshipMobileTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.LayoutDefinition.Screen)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                item {
                    BoardView(
                        board = state.boards.myBoard,
                        modifier = Modifier
                            .testTag(TestTags.LayoutDefinition.Board)
                            .padding(16.dp),
                        onSquareClicked = {}
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Button(
                            onClick = { handlers.onSubmitShotsClick() }
                        ) {
                            Text("Submit")
                        }

                        ProgressTimer(
                            timeToDefineShot,
                            onTimeout = handlers.onTimeout
                        )
                    }

                    BoardView(
                        board = state.boards.opponentBoard,
                        modifier = Modifier
                            .testTag(TestTags.LayoutDefinition.Board)
                            .padding(16.dp),
                        onSquareClicked = handlers.onOpponentBoardSquareClicked
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    val board = Board(
        side = 10,
        shots = listOf(),
        hits = listOf(),
        shipParts = listOf()
    )

    val gameBoards = GameBoards(board, board)

    ShotsDefinitionScreen(
        state = ShotsDefinitionScreenState(
            boards = gameBoards,
            turn = GameTurn.MY,
            remainingTime = 60L,
            timerResetToggle = false
        ),
        timeToDefineShot = 60L
    )
}