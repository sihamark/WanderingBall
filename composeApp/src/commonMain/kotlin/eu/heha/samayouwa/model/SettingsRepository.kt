package eu.heha.samayouwa.model

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import eu.heha.samayouwa.ui.ColorValue
import kotlinx.coroutines.flow.first
import okio.Path.Companion.toPath

class SettingsRepository(private val path: String) {
    private val dataStore = PreferenceDataStoreFactory.createWithPath { path.toPath() }

    suspend fun loadSettings(): Settings {
        val data = dataStore.data.first()
        val velocity = data[velocityKey] ?: return Settings()
        val size = data[sizeKey] ?: return Settings()
        val primaryColor = data[primaryColorKey]?.decodeColorValue() ?: return Settings()
        val backgroundColor = data[backgroundColorKey]?.decodeColorValue() ?: return Settings()
        return Settings(velocity.toFloat(), size.toFloat(), primaryColor, backgroundColor)
    }

    private fun String.decodeColorValue(): ColorValue {
        TODO()
    }

    private fun ColorValue.encodeToString(): String {
        TODO()
    }

    suspend fun saveSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[velocityKey] = settings.velocity.toDouble()
            preferences[sizeKey] = settings.size.toDouble()
            preferences[primaryColorKey] = settings.primaryColor.toString()
            preferences[backgroundColorKey] = settings.backgroundColor.toString()
        }
    }

    companion object {
        private val velocityKey = doublePreferencesKey("velocity")
        private val sizeKey = doublePreferencesKey("size")
        private val primaryColorKey = stringPreferencesKey("primaryColor")
        private val backgroundColorKey = stringPreferencesKey("backgroundColor")
    }
}