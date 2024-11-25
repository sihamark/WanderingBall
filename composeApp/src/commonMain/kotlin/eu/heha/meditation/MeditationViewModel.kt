package eu.heha.meditation

import androidx.annotation.FloatRange
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MeditationViewModel : ViewModel() {

    var state by mutableStateOf(State())
        private set

    private var hidingJob: Job? = null

    fun showSettingsButton() {
        state = state.copy(isSettingsButtonVisible = true)
        hidingJob?.cancel()
        hidingJob = viewmodelScope.launch {
            delay(2000)
            state = state.copy(isSettingsButtonVisible = false)
        }
    }

    data class State(
        val velocity: Float = 10f,
        @FloatRange(from = 0.0, to = 1.0)
        val position: Float = 0f,
        val isPlaying: Boolean = false,
        val isSettingsButtonVisible: Boolean = false,
        val isSettingsDialogVisible: Boolean = false
    )
}