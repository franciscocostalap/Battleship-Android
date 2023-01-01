package com.example.battleshipmobile.ui.views.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.R


val BATTLESHIP_IMAGE_SIZE = 160.dp

@Composable
fun BattleShipImage(
    size : Dp = BATTLESHIP_IMAGE_SIZE
){
    Image(
        painter = painterResource(id = R.drawable.bs_icon),
        contentDescription = "battleship icon",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .height(size)
    )
}