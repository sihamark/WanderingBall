package eu.heha.samayouwa

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
            rootPath = Path(
                File("data")
                    .also { it.mkdirs() }
                    .path
            )
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