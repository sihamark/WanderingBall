package eu.heha.samayouwa

import androidx.compose.runtime.Composable
import eu.heha.samayouwa.model.PropertiesSettingsDao
import eu.heha.samayouwa.ui.BounceRoute
import eu.heha.samayouwa.ui.theme.AppTheme
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

object SamayouwaApp {

    var debugString = ""

    private lateinit var requirements: Requirements

    private var onClickPreferences: (() -> Unit)? = null

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

    fun triggerPreferences() {
        onClickPreferences?.invoke()
    }

    fun registerOnClickPreferences(onClick: () -> Unit) {
        onClickPreferences = onClick
    }

    data class Requirements(
        val antilog: Antilog = DebugAntilog(),
        val settingsDaoFactory: (() -> PropertiesSettingsDao)? = null
    )
}