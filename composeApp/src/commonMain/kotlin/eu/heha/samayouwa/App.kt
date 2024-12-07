package eu.heha.samayouwa

import androidx.compose.runtime.Composable
import eu.heha.samayouwa.ui.BounceRoute
import eu.heha.samayouwa.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        BounceRoute()
    }
}