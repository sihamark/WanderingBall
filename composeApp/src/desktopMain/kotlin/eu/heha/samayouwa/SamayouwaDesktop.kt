package eu.heha.samayouwa

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import eu.heha.samayouwa.model.DataStoreSettingsDao
import kotlinx.io.files.Path
import org.jetbrains.compose.resources.painterResource
import samayouwa.composeapp.generated.resources.Res
import samayouwa.composeapp.generated.resources.icon
import java.io.File

fun main() {
    App.initialize(
        App.Requirements(
            settingsDaoFactory = {
                val jarFilePath = App::class.java.protectionDomain.codeSource.location.file
                    .replace("%20", " ")// as an uri it escapes spaces TODO: automate decode url

                val rootFile = File(jarFilePath)
                    .parentFile //app folder
                    .parentFile //root folder

                val dataFolder = File(rootFile, "data")
                    .also { it.mkdirs() }

                DataStoreSettingsDao(Path(dataFolder.path))
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