package eu.heha.samayouwa.ui

import androidx.annotation.FloatRange
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class BounceViewModel : ViewModel() {

    var state by mutableStateOf(State())
        private set

    private var quickSettingsHidingJob: Job? = null
    private var playJob: Job? = null
    private var isDecreasing = false

    fun play() {
        state = state.copy(isPlaying = true)
        playJob?.cancel()
        playJob = viewModelScope.launch(Dispatchers.Default) {
            var latestTimestamp = Clock.System.now()
            while (isActive) {
                delay(10)
                val adjust: Float.(Float) -> Float = if (isDecreasing) {
                    { minus(it) }
                } else {
                    { plus(it) }
                }
                val newTimeStamp = Clock.System.now()
                val delta = newTimeStamp.toEpochMilliseconds() -
                        latestTimestamp.toEpochMilliseconds()
                latestTimestamp = newTimeStamp
                val deltaPosition = state.velocity * (delta / 1000f)
                val newPosition = state.position.adjust(deltaPosition)
                val adjustedPosition = if (newPosition > 1) {
                    isDecreasing = true
                    1 - (newPosition - 1)
                } else if (newPosition < 0) {
                    isDecreasing = false
                    -newPosition
                } else {
                    newPosition
                }
                state = state.copy(position = adjustedPosition)
            }
        }
    }

    fun pause() {
        state = state.copy(isPlaying = false)
        playJob?.cancel()
    }

    fun showQuickSettings() {
        state = state.copy(isQuickSettingsVisible = true)
        quickSettingsHidingJob?.cancel()
        quickSettingsHidingJob = viewModelScope.launch {
            delay(2000)
            state = state.copy(isQuickSettingsVisible = false)
        }
    }

    fun showSettingsDialog() {
        state = state.copy(isSettingsDialogVisible = true)
    }

    fun hideSettingsDialog() {
        state = state.copy(isSettingsDialogVisible = false)
    }

    fun setVelocity(velocity: Float) {
        state = state.copy(velocity = velocity.coerceIn(velocityRange))
    }

    fun setSize(size: Float) {
        state = state.copy(size = size.coerceIn(sizeRange))
    }

    fun togglePlay() {
        if (state.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun setPrimaryColor(colorValue: ColorValue) {
        state = state.copy(primaryColor = colorValue)
    }

    fun setBackgroundColor(colorValue: ColorValue) {
        state = state.copy(backgroundColor = colorValue)
    }

    data class State(
        @FloatRange(from = VELOCITY_MIN.toDouble(), to = VELOCITY_MAX.toDouble())
        val velocity: Float = .5f,
        @FloatRange(from = 0.0, to = 1.0)
        val position: Float = 0f,
        @FloatRange(from = SIZE_MIN.toDouble(), to = SIZE_MAX.toDouble())
        val size: Float = 50f,
        val primaryColor: ColorValue = themeColor(ColorScheme::primary),
        val backgroundColor: ColorValue = themeColor(ColorScheme::background),
        val isPlaying: Boolean = false,
        val isQuickSettingsVisible: Boolean = false,
        val isSettingsDialogVisible: Boolean = false
    )

    companion object {
        const val VELOCITY_MIN = 0.001f
        const val VELOCITY_MAX = 10.0f
        val velocityRange = VELOCITY_MIN..VELOCITY_MAX

        const val SIZE_MIN = 10.0f
        const val SIZE_MAX = 300.0f
        val sizeRange = SIZE_MIN..SIZE_MAX

        val primaryColors = listOf(
            themeColor(ColorScheme::primary),
            themeColor(ColorScheme::secondary),
            themeColor(ColorScheme::tertiary),
            specificColor(0xFF000000), // Black
            specificColor(0xFFFFFFFF), // White
            specificColor(0xFFF44336), // Red 500
            specificColor(0xFFE91E63), // Pink 500
            specificColor(0xFF9C27B0), // Purple 500
            specificColor(0xFF673AB7), // Deep Purple 500
            specificColor(0xFF3F51B5), // Indigo 500
            specificColor(0xFF2196F3), // Blue 500
            specificColor(0xFF03A9F4), // Light Blue 500
            specificColor(0xFF00BCD4), // Cyan 500
            specificColor(0xFF009688), // Teal 500
            specificColor(0xFF4CAF50), // Green 500
            specificColor(0xFF8BC34A), // Light Green 500
            specificColor(0xFFCDDC39), // Lime 500
            specificColor(0xFFFFEB3B), // Yellow 500
            specificColor(0xFFFFC107), // Amber 500
            specificColor(0xFFFF9800), // Orange 500
            specificColor(0xFFFF5722), // Deep Orange 500
            specificColor(0xFF795548), // Brown 500
            specificColor(0xFF9E9E9E), // Grey 500
            specificColor(0xFF607D8B)  // Blue Grey 500
        )

        val backgroundColors = listOf(
            themeColor(ColorScheme::background),
            specificColor(0xFF000000), // Black
            specificColor(0xFFFFFFFF), // White
            specificColor(0xFFBDBDBD), // Grey 400
            specificColor(0xFF757575), // Grey 600
            specificColor(0xFF616161), // Grey 700
            specificColor(0xFF424242), // Grey 800
            specificColor(0xFF212121), // Grey 900
            specificColor(0xFFF44336), // Red 500
            specificColor(0xFFE91E63), // Pink 500
            specificColor(0xFF9C27B0), // Purple 500
            specificColor(0xFF673AB7), // Deep Purple 500
            specificColor(0xFF3F51B5), // Indigo 500
            specificColor(0xFF2196F3), // Blue 500
            specificColor(0xFF03A9F4), // Light Blue 500
            specificColor(0xFF00BCD4), // Cyan 500
            specificColor(0xFF009688), // Teal 500
            specificColor(0xFF4CAF50), // Green 500
            specificColor(0xFF8BC34A), // Light Green 500
            specificColor(0xFFCDDC39), // Lime 500
            specificColor(0xFFFFEB3B), // Yellow 500
            specificColor(0xFFFFC107), // Amber 500
            specificColor(0xFFFF9800), // Orange 500
            specificColor(0xFFFF5722), // Deep Orange 500
            specificColor(0xFF795548), // Brown 500
            specificColor(0xFF9E9E9E), // Grey 500
            specificColor(0xFF607D8B)  // Blue Grey 500
        )
    }
}