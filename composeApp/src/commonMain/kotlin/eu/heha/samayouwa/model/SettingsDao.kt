package eu.heha.samayouwa.model

interface SettingsDao {
    suspend fun loadSettings(): Settings
    suspend fun saveSettings(settings: Settings)
}