package eu.heha.samayouwa.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import eu.heha.samayouwa.model.ColorThemeToken

sealed interface ColorValue {
    data class Specific(val color: Color) : ColorValue
    data class Theme(val token: ColorThemeToken) : ColorValue
}

fun specificColor(color: Long) = ColorValue.Specific(Color(color))
fun themeColor(token: ColorThemeToken) = ColorValue.Theme(token)

@Composable
fun ColorValue.color(): Color = when (this) {
    is ColorValue.Specific -> color
    is ColorValue.Theme -> {
        val colorScheme = MaterialTheme.colorScheme
        token.fromTheme(colorScheme)
    }
}