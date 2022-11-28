package com.example.battleshipmobile.battleship.service.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Ship(val size: Int ,val name: String){

    @Composable
    fun Draw(tileSize : Dp){
        Row(modifier = Modifier.fillMaxWidth()) {
            Box ( modifier = Modifier
                .height(tileSize)
                .width(tileSize * size)
                .background(Blue)
            )
        }
    }

    @Composable
    fun DrawName(tileSize : Dp){
        Text(
            textAlign = TextAlign.End
            ,text = name, modifier = Modifier
            .height(tileSize)
            .width(tileSize)
        )
    }

}

