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
import com.example.battleshipmobile.battleship.play.BarColors
import com.example.battleshipmobile.battleship.play.Orientation
import com.example.battleshipmobile.ui.theme.Blue200
import com.example.battleshipmobile.ui.theme.Blue700


@Composable
fun CustomProgressBar(
    progress: Float,
    orientation: Orientation = Orientation.HORIZONTAL,
    barColor: BarColors = BarColors(Blue200, Blue700)
){
    if(orientation == Orientation.HORIZONTAL) {
        HorizontalProgressBar(progress = progress, barColor = barColor)
    } else {
        VerticalProgressBar(progress = progress, barColor = barColor)
    }

}

@Composable
private fun VerticalProgressBar(
    progress: Float,
    barColor: BarColors = BarColors(Blue200, Blue700)
) {
    Column (
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
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
                    .background(barColor.background)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(progress)
                    .clip(RoundedCornerShape(9.dp))
                    .background(barColor.foreground)
                    .animateContentSize()
            )
        }

    }
}

@Composable
private fun HorizontalProgressBar(
    progress: Float,
    barColor: BarColors = BarColors(Blue200, Blue700),
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
                    .background(barColor.background)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(barColor.foreground)
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

