import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import eu.heha.samayouwa.model.ColorThemeToken
import eu.heha.samayouwa.model.Settings
import eu.heha.samayouwa.model.SettingsDao
import eu.heha.samayouwa.ui.ColorValue
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.io.files.Path
import okio.Path.Companion.toPath
import sun.misc.Unsafe

class DataStoreSettingsDao(private val path: Path) : SettingsDao {
    private val dataStore = PreferenceDataStoreFactory.createWithPath { path.toString().toPath() }

    init {
        try {
            //TODO: remove this once release build works
            val unsafe = Unsafe.getUnsafe()
            Napier.e { "unsafe: $unsafe" }
        } catch (e: Throwable) {
            Napier.e(e) { "failed to create data store" }
        }
    }

    override suspend fun loadSettings(): Settings {
        val data = dataStore.data.first()
        val velocity = data[velocityKey] ?: run {
            Napier.e { "velocity not found" }
            return Settings()
        }
        val size = data[sizeKey] ?: run {
            Napier.e { "size not found" }
            return Settings()
        }
        val primaryColor = data[primaryColorKey]?.decodeColorValue() ?: return Settings()
        val backgroundColor = data[backgroundColorKey]?.decodeColorValue() ?: return Settings()
        return Settings(velocity.toFloat(), size.toFloat(), primaryColor, backgroundColor)
    }

    private fun String.decodeColorValue(): ColorValue? {
        if (startsWith("specific:")) {
            val colorString = substringAfter("specific:").take(8)
            val color = colorString.toLongOrNull(16) ?: run {
                Napier.e { "failed to parse color: $colorString" }
                return null
            }
            return ColorValue.Specific(Color(color))
        } else if (startsWith("theme:")) {
            val tokenRaw = substringAfter("theme:")
            val token = ColorThemeToken.entries.find { it.name == tokenRaw } ?: run {
                Napier.e { "unknown color theme token: $tokenRaw" }
                return null
            }
            return ColorValue.Theme(token)
        } else {
            Napier.e { "unknown color value: $this" }
            return null
        }
    }

    override suspend fun saveSettings(settings: Settings) {
        dataStore.edit { preferences ->
            preferences[velocityKey] = settings.velocity.toDouble()
            preferences[sizeKey] = settings.size.toDouble()
            preferences[primaryColorKey] = settings.primaryColor.encodeToString()
            preferences[backgroundColorKey] = settings.backgroundColor.encodeToString()
        }
    }

    private fun ColorValue.encodeToString(): String {
        return when (this) {
            is ColorValue.Specific -> "specific:${color.value.toString(16).take(8)}"
            is ColorValue.Theme -> "theme:${token.name}"
        }
    }

    companion object {
        private val velocityKey = doublePreferencesKey("velocity")
        private val sizeKey = doublePreferencesKey("size")
        private val primaryColorKey = stringPreferencesKey("primaryColor")
        private val backgroundColorKey = stringPreferencesKey("backgroundColor")
    }
}