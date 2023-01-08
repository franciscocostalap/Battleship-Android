package com.example.battleshipmobile.battleship.play.shotDefinition

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.R
import com.example.battleshipmobile.battleship.play.Orientation
import com.example.battleshipmobile.battleship.play.StatelessProgressBar
import com.example.battleshipmobile.battleship.play.TimerLogic
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.Square
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.game.BoardView
import com.example.battleshipmobile.ui.views.general.SlidingText

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
    val onOwnBoardSquareClicked : () -> Unit = {},
    val onSubmitShotsClick: () -> Unit = {},
    val onTimeout: () -> Unit = {},
)

data class ShotsDefinitionScreenState(
    val boards: GameBoards,
    val turn: GameTurn,
    val remainingTime: Long,
    val timerResetToggle: Boolean,
    val remainingShots: Int,
    val isSubmittingDisabled: Boolean
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
    var timerProgress by rememberSaveable { mutableStateOf(1.0F) }

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
                    padding = padding,
                    timerProgress = timerProgress
                )
            }
            else -> {
                PortraitShotsDefinitionScreen(
                    state = state,
                    handlers = handlers,
                    padding = padding,
                    timerProgress = timerProgress
                )
            }
        }
    }
    key(state.timerResetToggle){
        TimerLogic(
            timeToDefineLayout = timeToDefineShot,
            onProgressChange = {
                timerProgress = it
            },
            onTimeout = { handlers.onTimeout() },
        )
    }
}

@Composable
private fun LandScapeShotsDefinitionScreen(
    state: ShotsDefinitionScreenState,
    handlers: ShotsDefinitionHandlers = ShotsDefinitionHandlers(),
    padding: PaddingValues,
    timerProgress : Float
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
                        onSquareClicked = {handlers.onOwnBoardSquareClicked()}
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
                        onClick = { handlers.onSubmitShotsClick() },
                        enabled = state.isSubmittingDisabled,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ),
                        modifier = Modifier.scale(0.9f)
                    ) {
                        Text(stringResource(R.string.confirm))
                    }

                    val turn =
                        if (state.turn == GameTurn.MY)
                            stringResource(R.string.my_turn)
                        else
                            stringResource(R.string.opponent_turn)
                    Text(
                        turn,
                        fontSize = 14.sp,
                    )

                    Text(
                        stringResource(R.string.remaining_shots) + "${state.remainingShots}",
                        fontSize = 14.sp,
                    )

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
                        StatelessProgressBar(
                            progress = timerProgress,
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
    timerProgress : Float
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
                        onSquareClicked = {handlers.onOwnBoardSquareClicked()}
                    )
                }

            }

            Row(
                modifier = Modifier
                    .height(100.dp)
                    .padding(start = 16.dp, end = 16.dp)
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
                        onClick = { handlers.onSubmitShotsClick() },
                        enabled = state.isSubmittingDisabled,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        )
                    ) {
                        Text(stringResource(R.string.confirm))
                    }

                    val turn =
                        if (state.turn == GameTurn.MY)
                            stringResource(R.string.my_turn)
                        else
                            stringResource(R.string.opponent_turn)
                    SlidingText(
                        turn,
                        MaterialTheme.typography.body1.fontSize,
                    )

                    SlidingText(
                        stringResource(R.string.remaining_shots) + "${state.remainingShots}",
                        MaterialTheme.typography.body1.fontSize,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1.9f),
                    verticalArrangement = Arrangement.Center
                ) {
                    StatelessProgressBar(
                        progress = timerProgress,
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
        remainingShots = 1,
        isSubmittingDisabled = false
    )
    BattleshipMobileTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTags.LayoutDefinition.Screen)
        ) { padding ->
            PortraitShotsDefinitionScreen(
                state,
                padding = padding,
                timerProgress = 1f
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
            remainingShots = 1,
            isSubmittingDisabled = false
        ),
        timeToDefineShot = 60L
    )
}