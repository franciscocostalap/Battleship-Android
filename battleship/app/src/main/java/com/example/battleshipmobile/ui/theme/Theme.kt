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
        primary = blueDark,
        primaryVariant = gray400,
        background = blueMedium,
        surface = blueNormal,
        secondary = blueMedium300
)

private val LightColorPalette = lightColors(
        primary = Blue400,
        primaryVariant = Blue700,
        secondary = Teal200,
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
