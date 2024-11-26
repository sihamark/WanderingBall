package eu.heha.meditation.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

sealed interface ColorValue {
    data class Specific(val color: Color) : ColorValue
    data class Theme(val fromTheme: (ColorScheme) -> Color) : ColorValue
}

fun specificColor(color: Long) = ColorValue.Specific(Color(color))
fun themeColor(fromTheme: (ColorScheme) -> Color) = ColorValue.Theme(fromTheme)

@Composable
fun ColorValue.color(): Color = when (this) {
    is ColorValue.Specific -> color
    is ColorValue.Theme -> {
        val colorScheme = MaterialTheme.colorScheme
        fromTheme(colorScheme)
    }
}