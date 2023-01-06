package com.example.battleshipmobile.battleship.play.layoutDefinition

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.play.Orientation
import com.example.battleshipmobile.battleship.play.ProgressTimer
import com.example.battleshipmobile.battleship.play.StatelessProgressBar
import com.example.battleshipmobile.battleship.service.model.Board
import com.example.battleshipmobile.battleship.service.model.GameRules
import com.example.battleshipmobile.battleship.service.model.Square
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.game.BoardView
import com.example.battleshipmobile.ui.views.game.FleetCompositionControls
import com.example.battleshipmobile.ui.views.game.FleetCompositionView
import com.example.battleshipmobile.ui.views.game.ShipData
import com.example.battleshipmobile.ui.views.general.BackButton
import kotlinx.coroutines.delay

data class LayoutDefinitionHandlers(
    val onSquareClicked: (Square) -> Unit = {},
    val onShipClicked: (ShipData) -> Unit = {},
    val onRotateClicked: () -> Unit = {},
    val onFleetResetClicked: () -> Unit = {},
    val onSubmit: () -> Unit = {},
    val onTimeout: () -> Unit = {},
    val onBackClicked: () -> Unit = {},
)

data class LayoutDefinitionScreenState(
    val board: Board,
    val availableShips: List<ShipData>,
    val selectedShip: ShipData?,
    val isSubmittingDisabled: Boolean,
)

@Composable
fun LayoutDefinitionScreen(
    state: LayoutDefinitionScreenState,
    handlers: LayoutDefinitionHandlers = LayoutDefinitionHandlers(),
    timeToDefineLayout: Long
) {
    var progress by rememberSaveable { mutableStateOf(1.0F) }
    val timeoutSeconds = timeToDefineLayout / 1000

    var remainingTime by rememberSaveable { mutableStateOf(timeoutSeconds) }
    var isOver by rememberSaveable { mutableStateOf(false) }

    BattleshipMobileTheme {

            StatelessLayoutDefinitionScreen(
                state = state,
                handlers = handlers,
                screenConfiguration = LocalConfiguration.current,
                timerProgress = progress,
            )

    }

    LaunchedEffect(key1 = true){
        while(!isOver) {
            if (remainingTime == 0L) {
                handlers.onTimeout()
                isOver = true
            }
            delay(1000)
            val updatedTime = remainingTime - 1
            remainingTime = updatedTime.coerceAtLeast(0L)

            val newProgress = remainingTime / timeoutSeconds.toFloat()
            progress = newProgress.coerceAtLeast(0F)
        }
    }
}

@Composable
fun StatelessLayoutDefinitionScreen(
    state: LayoutDefinitionScreenState,
    handlers: LayoutDefinitionHandlers = LayoutDefinitionHandlers(),
    screenConfiguration : Configuration,
    timerProgress: Float
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.LayoutDefinition.Screen),
        backgroundColor = MaterialTheme.colors.background,
    ) { padding ->

        when (screenConfiguration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeLayoutDefinitionScreen(
                    state = state,
                    handlers = handlers,
                    padding = padding,
                    timerProgress = timerProgress
                )
            }
            else -> {
                PortraitLayoutDefinitionScreen(
                    state = state,
                    handlers = handlers,
                    padding = padding,
                    timerProgress = timerProgress
                )
            }
        }
    }
}

@Composable
private fun PortraitLayoutDefinitionScreen(
    state: LayoutDefinitionScreenState,
    handlers: LayoutDefinitionHandlers = LayoutDefinitionHandlers(),
    padding: PaddingValues,
    timerProgress: Float,
){
    LazyColumn(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                BoardView(
                    board = state.board,
                    modifier = Modifier
                        .testTag(TestTags.LayoutDefinition.Board)
                        .padding(16.dp),
                    onSquareClicked = handlers.onSquareClicked
                )
            }


            StatelessProgressBar(
                timerProgress,
                orientation = Orientation.HORIZONTAL
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FleetCompositionView(
                    availableShips = state.availableShips,
                    selectedShip = state.selectedShip,
                    onShipSelected = handlers.onShipClicked
                )
                FleetCompositionControls(
                    onRotateRequested = handlers.onRotateClicked,
                    onResetRequested = handlers.onFleetResetClicked,
                    onSubmitRequested = handlers.onSubmit,
                    isRotateEnabled = state.selectedShip != null,
                    isResetEnabled = state.board.shipParts.isNotEmpty(),
                    isSubmitEnabled = state.availableShips.isEmpty() && !state.isSubmittingDisabled,
                )
            }
            Box(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                BackButton(
                    onBackClick = handlers.onBackClicked,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

}

@Composable
private fun LandscapeLayoutDefinitionScreen(
    state: LayoutDefinitionScreenState,
    handlers: LayoutDefinitionHandlers = LayoutDefinitionHandlers(),
    padding : PaddingValues,
    timerProgress: Float
) {
    Row(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Start
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            BoardView(
                board = state.board,
                modifier = Modifier
                    .testTag(TestTags.LayoutDefinition.Board)
                    .padding(16.dp)
                    .weight(1f),
                onSquareClicked = handlers.onSquareClicked
            )
        }
        StatelessProgressBar(
            timerProgress,
            orientation = Orientation.VERTICAL
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                FleetCompositionView(
                    availableShips = state.availableShips,
                    selectedShip = state.selectedShip,
                    onShipSelected = handlers.onShipClicked
                )
                FleetCompositionControls(
                    onRotateRequested = handlers.onRotateClicked,
                    onResetRequested = handlers.onFleetResetClicked,
                    onSubmitRequested = handlers.onSubmit,
                    isRotateEnabled = state.selectedShip != null,
                    isResetEnabled = state.board.shipParts.isNotEmpty(),
                    isSubmitEnabled = state.availableShips.isEmpty() && !state.isSubmittingDisabled,
                )
            }
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ){
                BackButton(
                    onBackClick = handlers.onBackClicked,
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LayoutDefinitionScreen(
        state = LayoutDefinitionScreenState(
            board = Board(
                side = 10,
                shots = listOf(),
                hits = listOf(),
                shipParts = listOf()
            ),
            availableShips = GameRules.FleetComposition(
                name = "Default",
                composition = mapOf(
                    1 to 1,
                    2 to 2,
                    3 to 1,
                    4 to 1,
                    5 to 1
                )
            ).toList(),
            selectedShip = null,
            isSubmittingDisabled = true,
        ),
        timeToDefineLayout = 1000
    )
}