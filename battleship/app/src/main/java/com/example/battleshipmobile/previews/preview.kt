package com.example.battleshipmobile.previews

import com.example.battleshipmobile.battleship.info.InfoScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleshipmobile.battleship.home.HomeScreen


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){

    HomeScreen(
        onClick = {

        },
        {

        }

    )
}

@Preview
@Composable
fun CreditsPreview(){
    InfoScreen(
        onSendEmailRequested =  { },
        socials = emptyList()
    )
}
