package eu.heha.samayouwa

import androidx.compose.runtime.Composable
import eu.heha.samayouwa.model.SettingsRepository
import eu.heha.samayouwa.ui.BounceRoute
import eu.heha.samayouwa.ui.theme.AppTheme
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.io.files.Path

object App {

    private lateinit var requirements: Requirements

    fun initialize(requirements: Requirements) {
        Napier.base(requirements.antilog)
        this.requirements = requirements
        SettingsRepository.initialize(Path(requirements.rootFolder, "settings.preferences_pb"))
    }

    @Composable
    fun Content() {
        AppTheme {
            BounceRoute()
        }
    }

    data class Requirements(
        val antilog: Antilog = DebugAntilog(),
        val rootFolder: Path
    )
}