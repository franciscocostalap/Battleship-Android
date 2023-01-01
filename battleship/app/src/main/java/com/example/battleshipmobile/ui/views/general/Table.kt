package com.example.battleshipmobile.ui.views.general


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.battleshipmobile.battleship.service.ranking.PlayerStatisticsDTO
import com.example.battleshipmobile.ui.TestTags
import com.example.battleshipmobile.ui.isItemVisible

@Composable
fun LazyTable(modifier : Modifier = Modifier,
              headers : List<String>,
              cellsWeight : List<Float>,
              playerStats : List<PlayerStatisticsDTO>,
              width : Dp = 350.dp,
              height : Dp = 650.dp,
              backgroundColor : Color = MaterialTheme.colors.primary,
              borderColor : Color = MaterialTheme.colors.secondary,
              headerBackgroundColor : Color = MaterialTheme.colors.primaryVariant,
              headerBorderColor : Color = MaterialTheme.colors.secondary,
              borderWidth : Dp = 2.dp,
              headerFontSize : TextUnit = 13.sp,
              rowFontSize : TextUnit = 17.sp,
              dataListState : LazyListState = rememberLazyListState(),
){
    require(cellsWeight.size == headers.size) { "Headers and weights must have the same size" }

    StatelessLazyTable(
        headers = headers,
        cellsWeight = cellsWeight,
        playerStats = playerStats,
        listState = dataListState,
        width = width,
        height = height,
        backgroundColor = backgroundColor,
        borderColor = borderColor,
        headerBackgroundColor = headerBackgroundColor,
        headerBorderColor = headerBorderColor,
        borderWidth = borderWidth,
        headerFontSize = headerFontSize,
        rowFontSize = rowFontSize,
        modifier = modifier
    )


}

@Composable
fun StatelessLazyTable(
    modifier : Modifier = Modifier,
    headers : List<String>,
    cellsWeight : List<Float>,
    playerStats : List<PlayerStatisticsDTO>,
    listState : LazyListState,
    width : Dp = 350.dp,
    height : Dp = 650.dp,
    backgroundColor : Color = MaterialTheme.colors.primary,
    borderColor : Color = MaterialTheme.colors.secondary,
    headerBackgroundColor : Color = MaterialTheme.colors.primaryVariant,
    headerBorderColor : Color = MaterialTheme.colors.secondary,
    borderWidth : Dp = 2.dp,
    headerFontSize : TextUnit = 13.sp,
    rowFontSize : TextUnit = 17.sp
    ) {
    Column(
        modifier = modifier
            .width(width)
            .height(height)
            .background(backgroundColor)
            .border(borderWidth, borderColor)
    ) {

        //header
        TableRow(
            boxesText = headers,
            boxesWeight = cellsWeight,
            boxesBackground = headerBackgroundColor,
            boxesBorder = headerBorderColor,
            fontSize = headerFontSize,
            isVisible = true,
        )

        //values
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .testTag(TestTags.Statistics.Table),
            state = listState
        ) {
            items(playerStats.size) { index ->
                val stat = playerStats[index]
                TableRow(
                    boxesText = listOf(
                        "#" + stat.rank.toString(),
                        stat.player,
                        stat.totalGames.toString(),
                        stat.wins.toString()
                    ),
                    boxesWeight = cellsWeight,
                    fontSize = rowFontSize,
                    modifier = Modifier.testTag(TestTags.Statistics.TableRow+index.toString()),
                    isVisible = listState.isItemVisible(index)
                )
            }

        }
    }
}


@Composable
private fun TableRow(
    modifier: Modifier = Modifier,
    boxesBackground : Color = MaterialTheme.colors.primaryVariant,
    boxesBorder : Color = MaterialTheme.colors.secondary,
    boxesBorderWidth : Dp = 1.dp,
    rowHeight : Dp = 50.dp,
    boxesText: List<String>,
    boxesWeight : List<Float>,
    fontSize : TextUnit = 10.sp,
    isVisible : Boolean
) {
    assert(boxesWeight.size == boxesText.size) { "boxesWeight and boxesText must have the same size" }
    val boxModifier = Modifier
        .fillMaxHeight()
        .background(boxesBackground)
        .border(boxesBorderWidth, boxesBorder)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(rowHeight)
            .testTag(TestTags.Other.TableRow)

    ) {
        boxesText.forEachIndexed{ index, text ->
            Box(
                modifier = boxModifier.weight(boxesWeight[index]),
                contentAlignment = Alignment.Center
            ) {
                if (index == 1) {
                    SlidingText(
                        text,
                        fontSize,
                        isVisible
                    )
                } else {
                    Text(text, fontSize = fontSize)
                }
            }
        }

    }
}
