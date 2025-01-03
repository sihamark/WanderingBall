package eu.heha.samayouwa.model

import androidx.annotation.FloatRange
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
    val primaryColor: ColorValue = ColorValue.Theme(ColorThemeToken.primary),
    val backgroundColor: ColorValue = ColorValue.Theme(ColorThemeToken.background)
) {
    companion object {
        internal const val VELOCITY_MIN = 0.001f
        internal const val VELOCITY_MAX = 10.0f
        internal val velocityRange = VELOCITY_MIN..VELOCITY_MAX

        internal const val SIZE_MIN = 10.0f
        internal const val SIZE_MAX = 300.0f
        internal val sizeRange = SIZE_MIN..SIZE_MAX
    }
}