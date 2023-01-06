package com.example.battleshipmobile.battleship.play.shotDefinition

import com.example.battleshipmobile.R
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.play.Orientation
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
    val onOwnBoardSquareClicked : (Square) -> Unit = {},
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
        StatelessShotsDefinitionScreen(
            state = state,
            handlers = handlers,
            timeToDefineShot = timeToDefineShot
        )
    }

}

@Composable
private fun StatelessShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    timeToDefineShot: Long,
    screenConfiguration: Configuration = LocalConfiguration.current
){

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.LayoutDefinition.Screen),
        backgroundColor = MaterialTheme.colors.background,
    ) { padding ->

        when (screenConfiguration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandScapeShotsDefinitionScreen(
                    state = state,
                    handlers = handlers,
                    timeToDefineShot = timeToDefineShot,
                    padding = padding
                )
            }
            else -> {
                PortraitShotsDefinitionScreen(
                    state = state,
                    handlers = handlers,
                    timeToDefineShot = timeToDefineShot,
                    padding = padding
                )
            }
        }
    }
}

@Composable
private fun LandScapeShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    padding: PaddingValues,
    timeToDefineShot: Long
) {
    LazyRow(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    BoardView(
                        board = state.boards.myBoard,
                        modifier = Modifier
                            .testTag(TestTags.LayoutDefinition.Board)
                            .padding(16.dp),
                        onSquareClicked = {}
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { handlers.onSubmitShotsClick(state.currentShots) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    ) {
                        Text("Submit")
                    }
                }


                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ){ //NOT centered, it is the progressTimer fault
                        ProgressTimer(
                            timeToDefineShot,
                            onTimeout = handlers.onTimeout,
                            orientation = Orientation.VERTICAL,
                        )
                    }
                }


            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                item {
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

@Composable
fun PortraitShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    padding: PaddingValues,
    timeToDefineShot: Long
) {
    LazyColumn( //surrounded by lazycolumn to support small sized screens
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        item {
            LazyRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                item{
                    BoardView(
                        board = state.boards.myBoard,
                        modifier = Modifier
                            .testTag(TestTags.LayoutDefinition.Board)
                            .padding(16.dp),
                        onSquareClicked = {}
                    )
                }

            }

            Row(
                modifier = Modifier
                    .height(100.dp)
                    .padding(start = 16.dp,end = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { handlers.onSubmitShotsClick() }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }

                    val turn =
                        if (state.turn == GameTurn.MY)
                            stringResource(R.string.my_turn)
                        else
                            stringResource(R.string.opponent_turn)
                    Text(turn)


                    Text(stringResource(R.string.remaining_shots) + "${state.remainingShots}")
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(2f),
                    verticalArrangement = Arrangement.Center
                ) {
                    //mudar timer
                    ProgressTimer(
                        timeToDefineShot,
                        onTimeout = handlers.onTimeout
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                item{
                    BoardView(
                        board = state.boards.opponentBoard,
                        modifier = Modifier
                            .testTag(TestTags.LayoutDefinition.Board)
                            .padding(8.dp),
                        onSquareClicked = handlers.onOpponentBoardSquareClicked
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PortraitScreenPreview() {
    val board = Board(
        side = 10,
        shots = listOf(),
        hits = listOf(),
        shipParts = listOf()
    )
    val state = ShotsDefinitionScreenState(
        boards = GameBoards(
            myBoard = board,
            opponentBoard = board
        ),
        turn = GameTurn.MY,
        remainingTime = 0,
        timerResetToggle = false,
        remainingShots = 1
    )
    BattleshipMobileTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.LayoutDefinition.Screen)
        ) { padding ->
            PortraitShotsDefinitionScreen(
                state,
                timeToDefineShot = 1000,
                padding = padding
            )
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