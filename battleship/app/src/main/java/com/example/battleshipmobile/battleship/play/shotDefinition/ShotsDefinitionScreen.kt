package com.example.battleshipmobile.battleship.play.shotDefinition

import com.example.battleshipmobile.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val remainingShots: Int
)

@Composable
fun ShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    timeToDefineShot: Long,
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val turn =
                                if (state.turn == GameTurn.MY)
                                    stringResource(R.string.my_turn)
                                else
                                    stringResource(R.string.opponent_turn)
                            Text(turn)


                            Text(stringResource(R.string.remaining_shots) + "${state.remainingShots}")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Button(
                                onClick = { handlers.onSubmitShotsClick() }
                            ) {
                                Text(stringResource(R.string.confirm))
                            }

                            //TIMER
                        }
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
            timerResetToggle = false,
            remainingShots = 1
        ),
        timeToDefineShot = 60L
    )
}