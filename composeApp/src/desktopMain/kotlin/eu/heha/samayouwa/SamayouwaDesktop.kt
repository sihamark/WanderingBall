package eu.heha.samayouwa

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jetbrains.compose.resources.painterResource
import samayouwa.composeapp.generated.resources.Res
import samayouwa.composeapp.generated.resources.icon

fun main() {
    Napier.base(DebugAntilog())
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Samayou Wa",
            icon = painterResource(Res.drawable.icon)
        ) {
            App()
        }
    }
}