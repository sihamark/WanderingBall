package eu.heha.meditation.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BounceRoute() {
    val viewModel = viewModel { BounceViewModel() }

    BouncePane(
        state = viewModel.state,
        onClickShowQuickSettings = viewModel::showQuickSettings,
        onClickShowSettingsDialog = viewModel::showSettingsDialog,
        onClickHideSettingsDialog = viewModel::hideSettingsDialog,
        onClickTogglePlay = viewModel::togglePlay,
        onChangeVelocity = viewModel::setVelocity,
        onChangeSize = viewModel::setSize,
        onChangePrimaryColor = viewModel::setPrimaryColor,
        onChangeBackgroundColor = viewModel::setBackgroundColor
    )
}