package com.example.battleshipmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
        primary = Blue900,
        primaryVariant = Blue400,
        background = gray200,
        surface = gray300,
        secondary = Teal200
)

private val DarkBlueColorPalette = darkColors(
        primary = HEADER_COLOR,
        onPrimary = gray150,
        primaryVariant = gray400,
        background = blueNormal,
        secondary = blueMedium300,
        secondaryVariant = blueMedium200,
        onSecondary = GRAY_8,
        onBackground = HEADER_COLOR
)

private val LightColorPalette = lightColors(
        primary = Blue400,
        primaryVariant = dark_blue,
        secondary = Blue700,
        background = gray100,
        surface = gray100

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BattleshipMobileTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkBlueColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}
