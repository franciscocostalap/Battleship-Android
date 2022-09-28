package com.example.battleshipmobile.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.battleshipmobile.views.screens.LoginScreen
import com.example.battleshipmobile.views.gameScreen

@Preview(showBackground = true)
@Composable
fun GameScreenPreview(){
    gameScreen()
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}

