package eu.heha.meditation.ui

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
        const val VELOCITY_MAX = 2.0f
        val velocityRange = VELOCITY_MIN..VELOCITY_MAX

        const val SIZE_MIN = 10.0f
        const val SIZE_MAX = 100.0f
        val sizeRange = SIZE_MIN..SIZE_MAX

        val primaryColors = listOf(
            themeColor(ColorScheme::primary),
            themeColor(ColorScheme::secondary),
            themeColor(ColorScheme::tertiary),
            themeColor(ColorScheme::onPrimaryContainer),
            themeColor(ColorScheme::onSecondaryContainer),
            themeColor(ColorScheme::onTertiaryContainer),
            specificColor(0xfff52582),
            specificColor(0xff25f597),
            specificColor(0xff82f525),
            specificColor(0xfff59725),
            specificColor(0xfff52f25),
            specificColor(0xff253bf5),
            specificColor(0xfff5e025),
            specificColor(0xff7825f5)
        )

        val backgroundColors = listOf(
            themeColor(ColorScheme::background),
            themeColor(ColorScheme::surface),
            themeColor(ColorScheme::surfaceVariant),
            themeColor(ColorScheme::primaryContainer),
            themeColor(ColorScheme::secondaryContainer),
            themeColor(ColorScheme::tertiaryContainer),
            specificColor(0xfff52582),
            specificColor(0xff25f597),
            specificColor(0xff82f525),
            specificColor(0xfff59725),
            specificColor(0xfff52f25),
            specificColor(0xff253bf5),
            specificColor(0xfff5e025),
            specificColor(0xff7825f5)
        )
    }
}