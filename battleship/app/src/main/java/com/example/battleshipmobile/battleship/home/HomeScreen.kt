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

private val INFO_BUTTON_WIDTH = 250.dp
private val CREDITS_BUTTON_WIDTH = 270.dp
private val LOGIN_BUTTON_WIDTH = 225.dp
private val PLAY_BUTTON_WIDTH = 230.dp

private val BUTTONS_HEIGHT = 70.dp

val BATTLESHIP_IMAGE_SIZE = 160.dp

@Composable
fun HomeScreen(
    onLoginRequested: () -> Unit,
    onLogoutRequested: () -> Unit,
    onPlayRequested: () -> Unit,
    onCreditsRequested: () -> Unit,
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

            Spacer(Modifier.size(20.dp))


            if (!isLoggedIn) {
                LoggedOutStructure(
                    onLoginRequested = onLoginRequested,
                    onRankingRequested = onRankingRequested,
                    onCreditsRequested = onCreditsRequested,
                )
            }else{
                LoggedInStructure(
                    onLogoutRequested = onLogoutRequested,
                    onPlayRequested = onPlayRequested,
                    onCreditsRequested = onCreditsRequested,
                    onRankingRequested = onRankingRequested,
                )
            }
        }
    }
}


@Composable
fun LoggedOutStructure(
    onLoginRequested: () -> Unit,
    onRankingRequested: () -> Unit,
    onCreditsRequested: () -> Unit,

    ){
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
        fontSize = 27.sp,
        text = stringResource(R.string.sign_in),
        modifier = Modifier
            .testTag(TestTags.Home.SignInButton)
    )

    Spacer(Modifier.size(20.dp))
    RankingButton(
        onRankingRequested = onRankingRequested,
        fontSize = 27.sp,
    )
    CreditsButton(
        onCreditsRequested = onCreditsRequested,
        fontSize = 27.sp,
    )

}

@Composable
fun LoggedInStructure(
    onLogoutRequested: () -> Unit,
    onPlayRequested: () -> Unit,
    onCreditsRequested: () -> Unit,
    onRankingRequested: () -> Unit,
){
    TextButton(
        onClick = onPlayRequested,
        buttonWidth = PLAY_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        text = stringResource(R.string.play_label),
        modifier = Modifier
            .testTag(TestTags.Home.PlayButton)
    )

    Spacer(Modifier.size(20.dp))

    RankingButton(
        onRankingRequested = onRankingRequested,
    )
    CreditsButton(
        onCreditsRequested = onCreditsRequested,
    )

    Spacer(Modifier.size(20.dp))


    TextButton(
        onClick = onLogoutRequested,
        buttonWidth = LOGIN_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        text = stringResource(R.string.sign_out),
        modifier = Modifier
            .testTag(TestTags.Home.LogoutButton)
    )

}

@Composable
private fun CreditsButton(
    onCreditsRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 25.sp,
) {
    TextButton(
        onClick = onCreditsRequested,
        buttonWidth = CREDITS_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        fontSize = fontSize,
        text = stringResource(R.string.credits_label),
        modifier = modifier
            .testTag(TestTags.Home.CreditsButton)
    )
}

@Composable
private fun RankingButton(
    onRankingRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 25.sp,
) {
    TextButton(
        onClick = onRankingRequested,
        buttonWidth = INFO_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        fontSize = fontSize,
        text = stringResource(R.string.ranking_label),
        modifier = modifier
            .testTag(TestTags.Home.RankingsButton)
    )
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    BattleshipMobileTheme {
        HomeScreen(
            onLoginRequested = {},
            onLogoutRequested = {},
            onPlayRequested = {},
            onCreditsRequested = {},
            onRankingRequested = {},
            isLoggedIn = true
        )
    }
}