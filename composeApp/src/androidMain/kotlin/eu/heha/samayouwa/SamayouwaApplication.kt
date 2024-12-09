package eu.heha.samayouwa

import eu.heha.samayouwa.model.DataStoreSettingsDao
import android.app.Application
import kotlinx.io.files.Path

class SamayouwaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        App.initialize(
            App.Requirements(
                settingsDaoFactory = {
                    val path = filesDir.resolve("data")
                        .also { it.mkdirs() }
                        .let { Path(it.path) }
                    DataStoreSettingsDao(path)
                }
            )
        )
    }
}