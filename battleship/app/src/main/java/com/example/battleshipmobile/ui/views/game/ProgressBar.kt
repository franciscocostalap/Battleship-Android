package com.example.battleshipmobile.ui.views.game

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.battleshipmobile.battleship.play.Orientation
import com.example.battleshipmobile.ui.theme.Blue200
import com.example.battleshipmobile.ui.theme.Blue700


@Composable
fun CustomProgressBar(
    progress: Float,
    orientation: Orientation = Orientation.HORIZONTAL,
){
    if(orientation == Orientation.HORIZONTAL) {
        HorizontalProgressBar(progress = progress)
    } else {
        VerticalProgressBar(progress = progress)
    }

}

@Composable
private fun VerticalProgressBar(
    progress: Float
) {
    Row (
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
        ){

        Box(
            modifier= Modifier
                .fillMaxHeight()
                .width(17.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Blue200)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(progress)
                    .clip(RoundedCornerShape(9.dp))
                    .background(Blue700)
                    .animateContentSize()
            )
        }

    }
}

@Composable
private fun HorizontalProgressBar(
    progress: Float
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ){
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .height(17.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Blue200)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Blue700)
                    .animateContentSize()
            )
        }
    }
}


@Preview
@Composable
fun HorizontalProgressBarPreview() {
    HorizontalProgressBar(progress = 0.5f)
}

@Preview
@Composable
fun VerticalProgressBarPreview() {
    VerticalProgressBar(progress = 0.5f)
}

