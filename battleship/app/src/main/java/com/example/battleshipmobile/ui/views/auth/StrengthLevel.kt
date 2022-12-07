package com.example.battleshipmobile.ui.views.auth

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.battleshipmobile.R

enum class StrengthLevel(
    @StringRes val textId: Int,
    val color: Color
) {
    TOO_WEAK(R.string.strength_level_too_weak, Color.Black),
    WEAK(R.string.strength_level_weak, Color.Red),
    DECENT(R.string.strength_level_decent, Color.Yellow),
    STRONG(R.string.strength_level_strong, Color.Blue),
    VERY_STRONG(R.string.strength_level_very_strong, Color.Green);

    companion object {

        val minLevel = TOO_WEAK

        fun ofLevel(score: Int): StrengthLevel {

            val enumValues = values()

            return when{
                score < 0 -> minLevel
                score in enumValues.indices -> enumValues[score]
                else -> VERY_STRONG
            }
        }
    }
}