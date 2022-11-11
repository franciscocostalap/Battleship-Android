package com.example.battleshipmobile.battleship.home

import com.example.battleshipmobile.battleship.info.InfoScreenActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.battleshipmobile.R
import com.example.battleshipmobile.ui.theme.BattleshipMobileTheme

private val ROUNDED_CORNER_RATIO = 30.dp
private val PLAY_BUTTON_WIDTH = 350.dp
private val INFO_BUTTON_WIDTH = 250.dp
private val BUTTON_PADDING = 10.dp
private val FONT_SIZE = 40.sp
private val BACKGROUND_BUTTON_COLOR = Color.Red
private val TEXT_BUTTON_COLOR = Color.White
private val HEADER_COLOR = Color(23, 55, 76)
private val HEADER_SIZE = 60.sp


@Composable
fun TextButton(
    onClick: () -> Unit,
    buttonWidth: Dp,
    buttonPadding: Dp = BUTTON_PADDING,
    buttonBackgroundColour: Color = BACKGROUND_BUTTON_COLOR,
    buttonTextColour: Color = TEXT_BUTTON_COLOR,
    text: String,
    fontSize: TextUnit = FONT_SIZE,
    fontWeight: FontWeight = FontWeight.Bold,
    RoundedCornerRatio: Dp = ROUNDED_CORNER_RATIO
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(buttonWidth)
            .padding(buttonPadding),
        shape = RoundedCornerShape(RoundedCornerRatio),
        colors = ButtonDefaults.buttonColors(buttonBackgroundColour, buttonTextColour)
    ) {
        Text(
            text,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    }
}


class HomeScreenActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BattleshipMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(R.drawable.home_bg),
                            contentDescription = "background_image",
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Box(
                                Modifier.background(Color.White.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "BATTLESHIP",
                                    color = HEADER_COLOR,
                                    fontSize = HEADER_SIZE,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.size(200.dp))
                            TextButton(
                                onClick = {},
                                buttonWidth = PLAY_BUTTON_WIDTH,
                                text = "Play"
                            )

                            TextButton(
                                onClick = {},
                                buttonWidth = INFO_BUTTON_WIDTH,
                                text = "Ranking"
                            )

                            TextButton(
                                onClick = { InfoScreenActivity.navigate(this@HomeScreenActivity) },
                                buttonWidth = INFO_BUTTON_WIDTH,
                                text = "Credits"
                            )

                            Spacer(Modifier.size(100.dp))
                        }
                    }
                }
            }
        }

}

@Composable
fun HomeScreen(onClick: () -> Unit, creditsOnClick: () -> Unit) {

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.home_bg),
        contentDescription = "background_image",
        contentScale = ContentScale.FillBounds
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            Modifier.background(Color.White.copy(alpha = 0.6f))
        ){
            Text(
                text = "BATTLESHIP",
                color = HEADER_COLOR,
                fontSize = HEADER_SIZE,
                fontWeight = FontWeight.Bold
                )
        }

        Spacer(Modifier.size(200.dp))
        TextButton(
            onClick = onClick,
            buttonWidth = PLAY_BUTTON_WIDTH,
            text = "Play"
        )

        TextButton(
            onClick = onClick,
            buttonWidth = INFO_BUTTON_WIDTH,
            text = "Ranking"
        )

        TextButton(
            onClick = creditsOnClick,
            buttonWidth = INFO_BUTTON_WIDTH,
            text = "Credits"
        )

        Spacer(Modifier.size(100.dp))
    }
}