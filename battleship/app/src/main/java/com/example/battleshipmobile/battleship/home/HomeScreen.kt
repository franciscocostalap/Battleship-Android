package com.example.battleshipmobile.battleship.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.theme.HEADER_COLOR
import com.example.battleshipmobile.ui.views.general.CustomTextButton
import com.example.battleshipmobile.ui.views.home.BattleShipImage

private val INFO_BUTTON_WIDTH = 250.dp
private val CREDITS_BUTTON_WIDTH = 270.dp
private val LOGIN_BUTTON_WIDTH = 225.dp
private val PLAY_BUTTON_WIDTH = 230.dp
private val BUTTONS_HEIGHT = 70.dp



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
        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeHomeScreen(
                    onLoginRequested = onLoginRequested,
                    onLogoutRequested = onLogoutRequested,
                    onPlayRequested = onPlayRequested,
                    onCreditsRequested = onCreditsRequested,
                    onRankingRequested = onRankingRequested,
                    isLoggedIn = isLoggedIn
                )
            }
            else -> {
                PortraitHomeScreen(
                    onLoginRequested = onLoginRequested,
                    onLogoutRequested = onLogoutRequested,
                    onPlayRequested = onPlayRequested,
                    onCreditsRequested = onCreditsRequested,
                    onRankingRequested = onRankingRequested,
                    isLoggedIn = isLoggedIn
                )
            }
        }
    }
}

@Composable
private fun AppTitle(){
    Text(
        text = stringResource(R.string.app_name),
        color = HEADER_COLOR,
        fontSize = MaterialTheme.typography.h3.fontSize,
        fontWeight = FontWeight.Bold
    )
}
@Composable
private fun PortraitHomeScreen(
    onLoginRequested: () -> Unit,
    onLogoutRequested: () -> Unit,
    onPlayRequested: () -> Unit,
    onCreditsRequested: () -> Unit,
    onRankingRequested: () -> Unit,
    isLoggedIn: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        BattleShipImage()

        AppTitle()

        Spacer(Modifier.size(20.dp))


        if (!isLoggedIn) {
            Text(
                text = stringResource(R.string.welcome_message),
                color = Color(62, 66, 68),  //put in theme
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.size(8.dp))

            LoginButton(onLoginRequested = onLoginRequested)

            Spacer(Modifier.size(20.dp))
            RankingButton(
                onRankingRequested = onRankingRequested,
                fontSize = 27.sp,
            )
            CreditsButton(
                onCreditsRequested = onCreditsRequested,
                fontSize = 27.sp,
            )
        }else{
            PlayButton(onPlayRequested = onPlayRequested)

            Spacer(Modifier.size(20.dp))

            RankingButton(
                onRankingRequested = onRankingRequested,
            )
            CreditsButton(
                onCreditsRequested = onCreditsRequested,
            )

            Spacer(Modifier.size(20.dp))

            LogoutButton(onLogoutRequested = onLogoutRequested)
        }
    }
}

@Composable
private fun LandscapeHomeScreen(
    onLoginRequested: () -> Unit,
    onLogoutRequested: () -> Unit,
    onPlayRequested: () -> Unit,
    onCreditsRequested: () -> Unit,
    onRankingRequested: () -> Unit,
    isLoggedIn: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        BattleShipImage(140.dp)


        AppTitle()

        Row (
           modifier = Modifier
               .fillMaxWidth()
               .weight(1f),
           horizontalArrangement = Arrangement.SpaceBetween
        ){

            Column(
               modifier = Modifier.fillMaxHeight()
            ) {
                if(!isLoggedIn){
                    LoginButton(onLoginRequested = onLoginRequested)
                }else{
                    PlayButton(onPlayRequested = onPlayRequested)
                    LogoutButton(onLogoutRequested = onLogoutRequested)
                }
            }

            Box(
                modifier = Modifier.
                    fillMaxHeight()
                    .weight(2f)
            ){
                Text(
                    text = stringResource(R.string.welcome_message),
                    color = Color(62, 66, 68),  //put in theme
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                RankingButton(
                    onRankingRequested = onRankingRequested,
                    fontSize = 27.sp,
                )
                CreditsButton(
                    onCreditsRequested = onCreditsRequested,
                    fontSize = 27.sp,
                )
            }

        }
    }
}

@Composable
private fun LoginButton(
    onLoginRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 27.sp,
) {
    CustomTextButton(
        onClick = onLoginRequested,
        buttonWidth = LOGIN_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        fontSize = fontSize,
        text = stringResource(R.string.sign_in),
        modifier = modifier
            .testTag(TestTags.Home.SignInButton)
    )
}


@Composable
private fun LogoutButton(
    onLogoutRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize : TextUnit = 30.sp
) {
    CustomTextButton(
        onClick = onLogoutRequested,
        buttonWidth = LOGIN_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        text = stringResource(R.string.sign_out),
        fontSize = fontSize,
        modifier = modifier
            .testTag(TestTags.Home.LogoutButton)
    )
}


@Composable
private fun PlayButton(
    onPlayRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 30.sp,
) {
    CustomTextButton(
        onClick = onPlayRequested,
        buttonWidth = PLAY_BUTTON_WIDTH,
        buttonHeight = BUTTONS_HEIGHT,
        text = stringResource(R.string.play_label),
        fontSize = fontSize,
        modifier = modifier
            .testTag(TestTags.Home.PlayButton)
    )
}


@Composable
private fun CreditsButton(
    onCreditsRequested: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 25.sp,
) {
    CustomTextButton(
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
    CustomTextButton(
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