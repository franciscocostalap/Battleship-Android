package com.example.battleshipmobile.battleship.play.lobby


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.components.TextButton
import com.example.battleshipmobile.battleship.play.lobby.QueueState.*
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.views.BackPressHandler
import com.example.battleshipmobile.ui.views.LoadingIndicator

private val SPACER_DIM = 50.dp
const val MAX_NUM_PLAYERS = 2
private val BUTTON_HEIGHT = 70.dp
private val INFO_BUTTON_WIDTH = 250.dp
private const val WAITING_LOBBY_MESSAGE = "Searching for an opponent"
private const val FULL_LOBBY_MESSAGE = "Opponent Found, game is starting"


@Composable
fun QueueScreen(
    queueState: QueueState,
    onBackClick:  () -> Unit = {},
    onCancelClick: () -> Unit= {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .testTag(TestTags.Lobby.Screen),
    ) {
        val numPlayers = if (queueState == SEARCHING_OPPONENT) 1 else 2

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$numPlayers/$MAX_NUM_PLAYERS",
                modifier = Modifier.testTag(TestTags.Lobby.LobbyState)
            )
            Spacer(modifier = Modifier.height(SPACER_DIM))
            LoadingIndicator(tag = TestTags.Lobby.LoadingIndicator)
            Spacer(modifier = Modifier.height(SPACER_DIM))
            if (queueState == FULL){
                Text(
                    text = FULL_LOBBY_MESSAGE,
                    modifier = Modifier.testTag(TestTags.Lobby.StatusMessage)
                )
            } else {
                Text(
                    text = WAITING_LOBBY_MESSAGE,
                    modifier = Modifier.testTag(TestTags.Lobby.StatusMessage)
                )
                Spacer(modifier = Modifier.height(SPACER_DIM))
                TextButton(
                    onClick = onCancelClick,
                    buttonWidth = INFO_BUTTON_WIDTH,
                    buttonHeight = BUTTON_HEIGHT,
                    text = "Cancel",
                    modifier = Modifier.testTag(TestTags.Lobby.CancelButton)
                )
                BackPressHandler(onBackPressed = onBackClick)
            }
        }
    }
}

@Preview
@Composable
fun QueueScreenPreview() {
    BattleshipMobileTheme {
        QueueScreen(SEARCHING_OPPONENT, {}, {})
    }
}