package eu.heha.samayouwa

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    App.initialize(App.Requirements())
    ComposeViewport(document.body!!) {
        App.Content()
    }
}