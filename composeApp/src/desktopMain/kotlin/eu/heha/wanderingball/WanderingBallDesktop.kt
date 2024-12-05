@file:JvmName("WanderingBall")

package eu.heha.wanderingball

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun main() {
    Napier.base(DebugAntilog())
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Wandering Ball"
        ) {
            App()
        }
    }
}