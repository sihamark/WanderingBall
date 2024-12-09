package eu.heha.samayouwa

import eu.heha.samayouwa.model.DataStoreSettingsDao
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.io.files.Path
import org.jetbrains.compose.resources.painterResource
import samayouwa.composeapp.generated.resources.Res
import samayouwa.composeapp.generated.resources.icon
import java.io.File

fun main() {
    App.initialize(
        App.Requirements(
            settingsDaoFactory = {
                val path = File("data")
                    .also { it.mkdirs() }
                    .let { Path(it.path) }
                DataStoreSettingsDao(path)
            }
        )
    )
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Samayou Wa",
            icon = painterResource(Res.drawable.icon)
        ) {
            App.Content()
        }
    }
}