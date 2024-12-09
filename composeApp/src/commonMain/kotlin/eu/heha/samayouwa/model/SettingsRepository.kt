package eu.heha.samayouwa.model

import eu.heha.samayouwa.App
import eu.heha.samayouwa.ui.specificColor
import eu.heha.samayouwa.ui.themeColor
import io.github.aakira.napier.Napier

object SettingsRepository {

    private var settingsDao: SettingsDao? = App.settingsDaoFactory?.invoke()

    suspend fun loadSettings(): Settings = try {
        settingsDao?.loadSettings() ?: Settings()
    } catch (e: Exception) {
        Napier.e { "Failed to load settings" }
        Settings()
    }

    suspend fun saveSettings(settings: Settings) = try {
        settingsDao?.saveSettings(settings)
    } catch (e: Exception) {
        Napier.e(e) { "Failed to save settings" }
    }

    val primaryColors = listOf(
        themeColor(ColorThemeToken.primary),
        themeColor(ColorThemeToken.secondary),
        themeColor(ColorThemeToken.tertiary),
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
        themeColor(ColorThemeToken.background),
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