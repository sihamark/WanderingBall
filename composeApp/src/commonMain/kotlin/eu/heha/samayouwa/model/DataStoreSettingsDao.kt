package eu.heha.samayouwa.model

import androidx.compose.ui.graphics.Color
import eu.heha.samayouwa.ui.ColorValue
import io.github.aakira.napier.Napier
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

class DataStoreSettingsDao(folder: Path) : SettingsDao {

    private val settingsFile = Path(folder, SETTINGS_FILE_NAME)

    override suspend fun loadSettings(): Settings {
        val pathContent = SystemFileSystem.source(settingsFile).use { source ->
            source.buffered().readString()
        }
        val lines = pathContent.lines()
        val velocity =
            lines.find { it.startsWith(VELOCITY_KEY) }?.substringAfter("=")?.toDoubleOrNull()
                ?: run {
                    Napier.e { "failed to parse velocity" }
                    return Settings()
                }
        val size =
            lines.find { it.startsWith(SIZE_KEY) }?.substringAfter("=")?.toDoubleOrNull() ?: run {
                Napier.e { "failed to parse size" }
                return Settings()
            }
        val primaryColor =
            lines.find { it.startsWith(PRIMARY_COLOR_KEY) }?.substringAfter("=")?.decodeColorValue()
                ?: run {
                    Napier.e { "failed to parse primary color" }
                    return Settings()
                }
        val backgroundColor =
            lines.find { it.startsWith(BACKGROUND_COLOR_KEY) }?.substringAfter("=")
                ?.decodeColorValue() ?: run {
                Napier.e { "failed to parse background color" }
                return Settings()
            }
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
        val content = buildString {
            appendLine("$VELOCITY_KEY=${settings.velocity}")
            appendLine("$SIZE_KEY=${settings.size}")
            appendLine("$PRIMARY_COLOR_KEY=${settings.primaryColor.encodeToString()}")
            appendLine("$BACKGROUND_COLOR_KEY=${settings.backgroundColor.encodeToString()}")
        }
        SystemFileSystem.sink(settingsFile).buffered().use { sink ->
            sink.writeString(content)
        }
    }

    private fun ColorValue.encodeToString(): String {
        return when (this) {
            is ColorValue.Specific -> "specific:${color.value.toString(16).take(8)}"
            is ColorValue.Theme -> "theme:${token.name}"
        }
    }

    companion object {
        private const val VELOCITY_KEY = ("velocity")
        private const val SIZE_KEY = ("size")
        private const val PRIMARY_COLOR_KEY = ("primaryColor")
        private const val BACKGROUND_COLOR_KEY = ("backgroundColor")

        private const val SETTINGS_FILE_NAME = "settings.properties"
    }
}