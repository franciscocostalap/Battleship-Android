package com.example.battleshipmobile.previews

import Credits
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


import com.example.battleshipmobile.views.gameScreen
import com.example.battleshipmobile.views.screens.HomeScreenContent


@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    gameScreen()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){

    HomeScreenContent(
        onClick = {

        },
        {

        }

    )
}

@Preview
@Composable
fun CreditsPreview(){
    Credits(
        onSendEmailRequested =  { },
        socials = emptyList()
    )

}
