package eu.heha.samayouwa

import androidx.compose.runtime.Composable
import eu.heha.samayouwa.model.SettingsDao
import eu.heha.samayouwa.ui.BounceRoute
import eu.heha.samayouwa.ui.theme.AppTheme
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object App {

    var debugString = ""

    private lateinit var requirements: Requirements

    val settingsDaoFactory get() = requirements.settingsDaoFactory

    fun initialize(requirements: Requirements) {
        Napier.base(requirements.antilog)
        this.requirements = requirements
    }

    @Composable
    fun Content() {
        AppTheme {
            BounceRoute()
        }
    }

    data class Requirements(
        val antilog: Antilog = DebugAntilog(),
        val settingsDaoFactory: (() -> SettingsDao)? = null
    )
}