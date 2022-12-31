package com.example.battleshipmobile.ui.views.general

import androidx.compose.animation.core.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SlidingText(
    text: String,
    fontSize: TextUnit = 20.sp,
    isVisible: Boolean = false
) {
    val scroll = rememberScrollState(0)
    val shouldAnimate = rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = shouldAnimate.value, key2 = isVisible){

        //reset the scroll position if the animation was not completed
        if(scroll.value > 0) {
            shouldAnimate.value = !shouldAnimate.value
            scroll.scrollTo(0)
        }

        scroll.animateScrollTo(
            scroll.maxValue,
            animationSpec = tween(4000, 20, easing = CubicBezierEasing(0f,0f,0f,0f))
        )

        //reset the loop
        scroll.scrollTo(0)
        shouldAnimate.value = !shouldAnimate.value
    }

    Text(
        text = text,
        fontSize = fontSize,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier.horizontalScroll(scroll, false)
    )


}

@Preview
@Composable
fun TextSliderPreview() {
    SlidingText(
        text = "This is a text slider that has a lot of text"
    )
}