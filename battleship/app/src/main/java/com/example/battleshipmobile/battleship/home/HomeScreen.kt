package com.example.battleshipmobile.battleship.home

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.battleship.components.TextButton
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.HEADER_COLOR

private val PLAY_BUTTON_WIDTH = 230.dp
private val BUTTONS_HEIGHT = 70.dp
private val INFO_BUTTON_WIDTH = 250.dp
private val CREDITS_BUTTON_WIDTH = 270.dp
private val LOGIN_BUTTON_WIDTH = 225.dp
private val BATTLESHIP_IMAGE_SIZE = 180.dp

@Composable
fun HomeScreen(
    onLoginRequested: () -> Unit,
    onLogoutRequested: () -> Unit,
    onPlayRequested: () -> Unit,
    onInfoRequested: () -> Unit,
    onRankingRequested: () -> Unit,
    isLoggedIn: Boolean,
) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .testTag(TestTags.Home.Screen)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.bs_icon),
                contentDescription = "battleship icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BATTLESHIP_IMAGE_SIZE)
            )

            Text(
                text = stringResource(R.string.app_name),
                color = HEADER_COLOR,
                fontSize = MaterialTheme.typography.h3.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.size(50.dp))

            if (!isLoggedIn) {
                Text(
                    text = stringResource(R.string.welcome_message),
                    color = Color(62, 66, 68),  //put in theme
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.size(8.dp))

                TextButton(
                    onClick = onLoginRequested,
                    buttonWidth = LOGIN_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    fontSize = 25.sp,    // pu
                    text = stringResource(R.string.sign_in),
                    modifier = Modifier
                        .testTag(TestTags.Home.SignInButton)
                )
            }

            if (isLoggedIn) {
                TextButton(
                    onClick = onPlayRequested,
                    buttonWidth = PLAY_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    text = stringResource(R.string.play_label),
                    modifier = Modifier
                        .testTag(TestTags.Home.PlayButton)
                )
            }

            if (isLoggedIn) {
                Spacer(Modifier.size(70.dp))

                TextButton(
                    onClick = onLogoutRequested,
                    buttonWidth = LOGIN_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    text = stringResource(R.string.sign_out),
                    modifier = Modifier
                        .testTag(TestTags.Home.LogoutButton)
                )
            }

            Spacer(Modifier.size(70.dp))
            TextButton(
                onClick = onRankingRequested,
                buttonWidth = INFO_BUTTON_WIDTH,
                buttonHeight = BUTTONS_HEIGHT,
                text = stringResource(R.string.ranking_label),
                modifier = Modifier
                    .testTag(TestTags.Home.RankingsButton)
            )

            TextButton(
                onClick = onInfoRequested,
                buttonWidth = CREDITS_BUTTON_WIDTH,
                buttonHeight = BUTTONS_HEIGHT,
                text = stringResource(R.string.credits_label),
                modifier = Modifier
                    .testTag(TestTags.Home.CreditsButton)
            )

            Spacer(Modifier.size(100.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BattleshipMobileTheme {
        HomeScreen(
            onLoginRequested = {},
            onLogoutRequested = {},
            onPlayRequested = {},
            onInfoRequested = {},
            onRankingRequested = {},
            isLoggedIn = true
        )
    }
}