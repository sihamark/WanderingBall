package eu.heha.samayouwa.model

import androidx.annotation.FloatRange
import androidx.compose.material3.ColorScheme
import eu.heha.samayouwa.ui.ColorValue

data class Settings(
    @FloatRange(
        from = VELOCITY_MIN.toDouble(),
        to = VELOCITY_MAX.toDouble()
    )
    val velocity: Float = 0.5f,
    @FloatRange(
        from = SIZE_MIN.toDouble(),
        to = SIZE_MAX.toDouble()
    )
    val size: Float = 50f,
    val primaryColor: ColorValue = ColorValue.Theme(ColorScheme::primary),
    val backgroundColor: ColorValue = ColorValue.Theme(ColorScheme::background)
) {
    companion object {
        const val VELOCITY_MIN = 0.001f
        const val VELOCITY_MAX = 10.0f
        val velocityRange = VELOCITY_MIN..VELOCITY_MAX

        const val SIZE_MIN = 10.0f
        const val SIZE_MAX = 300.0f
        val sizeRange = SIZE_MIN..SIZE_MAX
    }
}