package com.example.battleshipmobile.battleship.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val BUTTON_PADDING = 12.dp
private val BUTTON_BORDER_WIDTH = 3.dp
private val BUTTON_BORDER_COLOR = Color(0xFF000000)
private val FONT_SIZE = 30.sp
private val BACKGROUND_BUTTON_COLOR = Color(62, 66, 68 )
private val TEXT_BUTTON_COLOR = Color(23, 55, 76)
private val BUTTON_CORNER_BOXES_COLOR = Color(22, 45, 60, 255)
private val BUTTON_CORNER_BOXES_SIZE = 12.dp

@Composable
fun CustomBox(
    width: Dp,
    height : Dp,
    modifier: Modifier = Modifier,
    padding: Dp = BUTTON_PADDING,
    backgroundColor: Color = BUTTON_CORNER_BOXES_COLOR,
    cornerBoxesSize : Dp = BUTTON_CORNER_BOXES_SIZE,
    borderColor: Color = BUTTON_BORDER_COLOR,
    borderWidth: Dp = BUTTON_BORDER_WIDTH,
    content: @Composable() (BoxScope.() -> Unit)
) {
    BoxWithConstraints(
        modifier = modifier
            .width(width)
            .height(height)
            .padding(padding)
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = MaterialTheme.shapes.medium
            )
    ) {

        content()

        AlignedBox(
            backgroundColor = backgroundColor,
            borderColor  = borderColor,
            alignment = Alignment.TopEnd,
            size = cornerBoxesSize
        )
        AlignedBox(
            backgroundColor = backgroundColor,
            borderColor  = borderColor,
            alignment = Alignment.TopStart,
            size = cornerBoxesSize
        )
        AlignedBox(
            backgroundColor = backgroundColor,
            borderColor  = borderColor,
            alignment = Alignment.BottomStart ,
            size = cornerBoxesSize
        )
        AlignedBox(
            backgroundColor = backgroundColor,
            borderColor  = borderColor,
            alignment = Alignment.BottomEnd,
            size = cornerBoxesSize
        )
    }
}

@Composable
private fun BoxScope.AlignedBox(
    backgroundColor : Color = BUTTON_CORNER_BOXES_COLOR,
    borderColor : Color = BUTTON_BORDER_COLOR,
    size: Dp = BUTTON_CORNER_BOXES_SIZE,
    cornerRadius: Dp = BUTTON_BORDER_WIDTH,
    alignment : Alignment,
) {

    Box(
        modifier =
        Modifier
            .align(alignment)
            .height(size)
            .width(size)
            .background(backgroundColor)
            .border(
                width = cornerRadius,
                color = borderColor,
            )
    )
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    buttonWidth: Dp,
    buttonHeight : Dp,
    buttonPadding: Dp = BUTTON_PADDING,
    buttonBackgroundColour: Color = BACKGROUND_BUTTON_COLOR,
    buttonBorderColour: Color = BUTTON_BORDER_COLOR,
    buttonBorderWidth: Dp = BUTTON_BORDER_WIDTH,
    buttonTextColour: Color = TEXT_BUTTON_COLOR,
    text: String,
    fontSize: TextUnit = FONT_SIZE,
    fontWeight: FontWeight = FontWeight.Bold,
    modifiers : Modifier = Modifier
) {
    CustomBox(
        width = buttonWidth,
        height = buttonHeight,
        padding = buttonPadding,
        backgroundColor = buttonBackgroundColour,
        borderColor = buttonBorderColour,
        borderWidth = buttonBorderWidth,
        cornerBoxesSize = BUTTON_CORNER_BOXES_SIZE,
        modifier = modifiers

    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        ) {
            Text(
                text = text,
                color = buttonTextColour,
                fontSize = fontSize,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}