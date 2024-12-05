package eu.heha.wanderingball

import androidx.compose.runtime.Composable
import eu.heha.wanderingball.ui.BounceRoute
import eu.heha.wanderingball.ui.MeditationTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MeditationTheme {
        BounceRoute()
    }
}