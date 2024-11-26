package eu.heha.meditation

import androidx.compose.runtime.Composable
import eu.heha.meditation.ui.BounceRoute
import eu.heha.meditation.ui.MeditationTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MeditationTheme {
        BounceRoute()
    }
}