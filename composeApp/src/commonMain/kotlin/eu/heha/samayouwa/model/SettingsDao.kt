package eu.heha.samayouwa.model

interface SettingsDao {
    suspend fun saveSettings(settings: Settings)
    suspend fun loadSettings(): Settings
}