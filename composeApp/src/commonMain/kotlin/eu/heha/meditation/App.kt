package eu.heha.meditation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val viewModel = viewModel { MeditationViewModel() }
    val state = viewModel.state
    MeditationTheme {
        Scaffold {
            var parentWidth by remember { mutableStateOf(0f) }
            val density = LocalDensity.current
            LaunchedEffect(Unit) {
                viewModel.play()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(interactionSource = null, indication = null) {
                        viewModel.showSettingsButton()
                    }
                    .onGloballyPositioned {
                        parentWidth = with(density) { it.size.width.toDp().value }
                    }
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(state.size.dp)
                        .offset(x = ((parentWidth - state.size) * state.position).dp)
                        .align(Alignment.CenterStart)
                ) {
                    // Empty
                }

                AnimatedVisibility(
                    visible = state.isSettingsButtonVisible,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    IconButton(onClick = viewModel::showSettingsDialog) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }

                if (state.isSettingsDialogVisible) {
                    Dialog(onDismissRequest = viewModel::hideSettingsDialog) {
                        Surface(
                            modifier = Modifier.padding(16.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(
                                Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Velocity: ${state.velocity}")
                                Slider(
                                    value = state.velocity,
                                    onValueChange = {
                                        viewModel.setVelocity(it)
                                    },
                                    valueRange = MeditationViewModel.velocityRange
                                )
                                Spacer(Modifier.height(8.dp))
                                Text("Size: ${state.size}")
                                Slider(
                                    value = state.size,
                                    onValueChange = {
                                        viewModel.setSize(it)
                                    },
                                    valueRange = MeditationViewModel.sizeRange
                                )
                                Spacer(Modifier.height(8.dp))
                                OutlinedButton(viewModel::togglePlay) {
                                    Text(if (state.isPlaying) "Pause" else "Play")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}