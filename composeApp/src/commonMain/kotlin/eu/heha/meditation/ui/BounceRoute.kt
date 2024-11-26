package eu.heha.meditation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.heha.meditation.MeditationViewModel

@Composable
fun BounceRoute() {
    val viewModel = viewModel { MeditationViewModel() }

    LaunchedEffect(Unit) {
        viewModel.play()
    }

    BouncePane(
        state = viewModel.state,
        onClickShowQuickSettings = viewModel::showQuickSettings,
        onClickShowSettingsDialog = viewModel::showSettingsDialog,
        onClickHideSettingsDialog = viewModel::hideSettingsDialog,
        onClickTogglePlay = viewModel::togglePlay,
        onChangeVelocity = viewModel::setVelocity,
        onChangeSize = viewModel::setSize
    )
}