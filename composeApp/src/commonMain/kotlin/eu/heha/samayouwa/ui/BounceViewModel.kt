package eu.heha.samayouwa.ui

import androidx.annotation.FloatRange
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.heha.samayouwa.model.Settings
import eu.heha.samayouwa.model.SettingsRepository
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

    init {
        viewModelScope.launch {
            val settings = SettingsRepository.loadSettings()
            state = state.copy(settings = settings)
        }
    }

    private fun play() {
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
                val deltaPosition = state.settings.velocity * (delta / 1000f)
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

    fun togglePlay() {
        if (state.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun setVelocity(velocity: Float) {
        setSettings { copy(velocity = velocity.coerceIn(Settings.velocityRange)) }
    }

    fun setSize(size: Float) {
        setSettings { copy(size = size.coerceIn(Settings.sizeRange)) }
    }

    fun setPrimaryColor(colorValue: ColorValue) {
        setSettings { copy(primaryColor = colorValue) }
    }

    fun setBackgroundColor(colorValue: ColorValue) {
        setSettings { copy(backgroundColor = colorValue) }
    }

    fun resetSettings() {
        setSettings { Settings() }
    }

    private fun setSettings(mutateSettings: Settings.() -> Settings) {
        state = state.copy(
            settings = state.settings.mutateSettings()
        )
        viewModelScope.launch(Dispatchers.Default) {
            SettingsRepository.saveSettings(state.settings)
        }
    }

    data class State(
        @FloatRange(from = 0.0, to = 1.0)
        val position: Float = 0f,
        val isPlaying: Boolean = false,
        val isQuickSettingsVisible: Boolean = false,
        val isSettingsDialogVisible: Boolean = false,
        val settings: Settings = Settings(),
    )
}