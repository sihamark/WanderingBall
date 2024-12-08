package eu.heha.samayouwa

import android.app.Application
import kotlinx.io.files.Path

class SamayouwaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        App.initialize(
            App.Requirements(
                rootFolder = Path(
                    filesDir.resolve("data")
                        .also { it.mkdirs() }
                        .path
                )
            )
        )
    }
}