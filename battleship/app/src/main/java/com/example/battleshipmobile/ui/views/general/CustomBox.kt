package com.example.battleshipmobile.ui.views.general

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
private val BUTTON_CORNER_BOXES_COLOR = Color(22, 45, 60, 255)
private val BUTTON_CORNER_BOXES_SIZE = 12.dp

@Composable
fun CustomBox(
    width: Dp,
    height : Dp,
    modifier: Modifier = Modifier,
    padding: Dp = BUTTON_PADDING,
    backgroundColor: Color = MaterialTheme.colors.primary,
    cornerBoxesSize : Dp = BUTTON_CORNER_BOXES_SIZE,
    borderColor: Color = BUTTON_BORDER_COLOR,
    borderWidth: Dp = BUTTON_BORDER_WIDTH,
    content: @Composable (BoxScope.() -> Unit)
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
            borderColor  = borderColor,
            alignment = Alignment.TopEnd,
            size = cornerBoxesSize
        )
        AlignedBox(
            borderColor  = borderColor,
            alignment = Alignment.TopStart,
            size = cornerBoxesSize
        )
        AlignedBox(
            borderColor  = borderColor,
            alignment = Alignment.BottomStart ,
            size = cornerBoxesSize
        )
        AlignedBox(
            borderColor  = borderColor,
            alignment = Alignment.BottomEnd,
            size = cornerBoxesSize
        )
    }
}

@Composable
fun CustomTextButton(
    onClick: () -> Unit,
    buttonWidth: Dp,
    buttonHeight : Dp,
    modifier : Modifier = Modifier,
    buttonPadding: Dp = BUTTON_PADDING,
    buttonBackgroundColour: Color = MaterialTheme.colors.primary,
    buttonBorderColour: Color = BUTTON_BORDER_COLOR,
    buttonBorderWidth: Dp = BUTTON_BORDER_WIDTH,
    buttonTextColour: Color = MaterialTheme.colors.onPrimary,
    text: String,
    fontSize: TextUnit = FONT_SIZE,
    fontWeight: FontWeight = FontWeight.Bold,

    ) {

    CustomBox(
        width = buttonWidth,
        height = buttonHeight,
        padding = buttonPadding,
        backgroundColor = buttonBackgroundColour,
        borderColor = buttonBorderColour,
        borderWidth = buttonBorderWidth,
        cornerBoxesSize = BUTTON_CORNER_BOXES_SIZE,
        modifier = modifier

    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    enabled = true,
                    onClick = onClick
                )
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