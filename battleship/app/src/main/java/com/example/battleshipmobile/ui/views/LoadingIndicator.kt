package com.example.battleshipmobile.ui.views

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A circular loading indicator.
 * @param size the size of the indicator
 * @param sweepAngle the angle (length) of the indicator arc
 * @param color the color of indicator arc line
 * @param strokeWidth the width of the circle and arc lines
 */
@Composable
fun LoadingIndicator(
    tag: String,
    size: Dp = 32.dp,
    sweepAngle: Float = 90f,
    color: Color = MaterialTheme.colors.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth
) {

    // TODO: Consider the use of CircularProgressIndicator built in composable.

    //-*-*-*-*-*-*- animation -*-*-*-*-*-*-

    // Docs recommend to use transition animation for infinite loops
    // https://developer.android.com/jetpack/compose/animation
    val transition = rememberInfiniteTransition()

    // Define the changing value from 0 to 360.
    // This is the angle of the beginning of indicator arc
    // this value will change over time from 0 to 360 and repeat indefinitely.
    // It changes starting position of the indicator arc and the animation is obtained
    val currentArcStartAngle by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1100,
                easing = LinearEasing
            )
        )
    )

    //-*-*-*-*-*-*- draw -*-*-*-*-*-*-

    // define stroke with given width and arc ends type considering device DPI
    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }

    Canvas(
        Modifier
            .progressSemantics() // (optional) for Accessibility services
            .size(size) // canvas size
            .padding(strokeWidth / 2)
            .testTag(tag)//padding. otherwise, not the whole circle will fit in the canvas
    ) {
        // draw "background" (gray) circle with defined stroke.
        // without explicit center and radius it fit canvas bounds
        drawCircle(Color.LightGray, style = stroke)

        // draw arc with the same stroke
        drawArc(
            color,
            // arc start angle
            // -90 shifts the start position towards the y-axis
            startAngle = currentArcStartAngle.toFloat() - 90,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}