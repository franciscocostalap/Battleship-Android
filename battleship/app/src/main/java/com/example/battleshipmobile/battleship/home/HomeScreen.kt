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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme
import com.example.battleshipmobile.battleship.components.TextButton
import com.example.battleshipmobile.ui.TestTags

private val PLAY_BUTTON_WIDTH = 230.dp
private val BUTTONS_HEIGHT = 70.dp
private val INFO_BUTTON_WIDTH = 250.dp
private val CREDITS_BUTTON_WIDTH = 270.dp
private val HEADER_COLOR = Color(23, 55, 76)
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
            //  verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.bs_icon),
                contentDescription = "battleship icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(BATTLESHIP_IMAGE_SIZE)
            )
            Box {
                Text(
                    text = "BATTLESHIP",
                    color = HEADER_COLOR,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.size(50.dp))
            if(!isLoggedIn){
                Text(
                    text = "Welcome, please sign in to play!",
                    color = Color(62, 66, 68 ),  //put in theme
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.size(8.dp))
                TextButton(
                    onClick = onLoginRequested,
                    buttonWidth = LOGIN_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    fontSize = 25.sp,
                    text = "Sign In",
                    modifier=Modifier.testTag(TestTags.Home.SignInButton)
                )
            }
            if(isLoggedIn){
                TextButton(
                    onClick = onPlayRequested ,
                    buttonWidth = PLAY_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    text = "Play",
                    modifier=Modifier.testTag(TestTags.Home.PlayButton)
                )
            }
            Spacer(Modifier.size(70.dp))
            TextButton(
                onClick = onRankingRequested,
                buttonWidth = INFO_BUTTON_WIDTH,
                buttonHeight = BUTTONS_HEIGHT,
                text = "Ranking",
                modifier=Modifier.testTag(TestTags.Home.RankingsButton)
            )
            TextButton(
                onClick = onInfoRequested,
                buttonWidth = CREDITS_BUTTON_WIDTH,
                buttonHeight = BUTTONS_HEIGHT,
                text = "Credits",
                modifier=Modifier.testTag(TestTags.Home.CreditsButton)
            )
            if(isLoggedIn){
                Spacer(Modifier.size(70.dp))
                TextButton(
                    onClick = onLogoutRequested,
                    buttonWidth = LOGIN_BUTTON_WIDTH,
                    buttonHeight = BUTTONS_HEIGHT,
                    text = "Logout",
                    modifier=Modifier.testTag(TestTags.Home.LogoutButton)
                )
            }
            Spacer(Modifier.size(100.dp))

        }
    }
}

@Composable
fun IconButton(
    icon : Int,
    description : String,
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            modifier = Modifier.size(size)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
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