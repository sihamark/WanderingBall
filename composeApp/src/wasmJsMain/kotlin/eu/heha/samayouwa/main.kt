package eu.heha.samayouwa

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    SamayouwaApp.initialize(SamayouwaApp.Requirements())
    ComposeViewport(document.body!!) {
        SamayouwaApp.Content()
    }
}