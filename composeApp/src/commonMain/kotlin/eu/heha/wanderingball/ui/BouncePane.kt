package eu.heha.wanderingball.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.heha.wanderingball.ui.BounceViewModel.Companion.backgroundColors
import eu.heha.wanderingball.ui.BounceViewModel.Companion.primaryColors

@Composable
fun BouncePane(
    state: BounceViewModel.State,
    onClickShowQuickSettings: () -> Unit,
    onClickShowSettingsDialog: () -> Unit,
    onClickHideSettingsDialog: () -> Unit,
    onClickTogglePlay: () -> Unit,
    onChangeVelocity: (Float) -> Unit,
    onChangeSize: (Float) -> Unit,
    onChangePrimaryColor: (ColorValue) -> Unit,
    onChangeBackgroundColor: (ColorValue) -> Unit
) {
    val parentFocus = remember { FocusRequester() }
    Scaffold(
        containerColor = state.backgroundColor.color(),
        modifier = Modifier
            .focusRequester(parentFocus)
            .onKeyEvent {
                if (it.key == Key.Spacebar) {
                    if (it.type == KeyEventType.KeyDown) {
                        onClickTogglePlay()
                    }
                } else if (it.key != Key.Unknown) {
                    onClickShowQuickSettings()
                }
                false
            }
            .focusable(true)
    ) {
        var parentWidth by remember { mutableStateOf(0f) }
        val density = LocalDensity.current
        LaunchedEffect(
            state.isPlaying,
            state.isQuickSettingsVisible,
            state.isSettingsDialogVisible
        ) {
            parentFocus.requestFocus()
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = onClickShowQuickSettings
                )
                .onGloballyPositioned {
                    parentWidth = with(density) { it.size.width.toDp().value }
                }
        ) {
            ColorBlob(
                color = state.primaryColor.color(),
                size = state.size,
                modifier = Modifier
                    .offset(x = ((parentWidth - state.size) * state.position).dp)
                    .align(Alignment.CenterStart)
            )

            AnimatedVisibility(
                visible = state.isQuickSettingsVisible,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it },
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(Modifier.fillMaxWidth()) {
                    QuickSettings(
                        isPlaying = state.isPlaying,
                        onClickShowSettings = onClickShowSettingsDialog,
                        onClickTogglePlayPause = onClickTogglePlay,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }

            if (state.isSettingsDialogVisible) {
                SettingsDialog(
                    onClickHideSettingsDialog = onClickHideSettingsDialog,
                    velocity = state.velocity,
                    onChangeVelocity = onChangeVelocity,
                    size = state.size,
                    onChangeSize = onChangeSize,
                    primaryColor = state.primaryColor,
                    onPrimaryColorChange = onChangePrimaryColor,
                    backgroundColor = state.backgroundColor,
                    onBackgroundColorChange = onChangeBackgroundColor
                )
            }
        }
    }
}

@Composable
private fun QuickSettings(
    isPlaying: Boolean,
    onClickShowSettings: () -> Unit,
    onClickTogglePlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .safeContentPadding()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            val icon = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow
            IconButton(onClick = onClickTogglePlayPause) {
                Icon(icon, contentDescription = "Play/Pause")
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onClickShowSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    }
}

@Composable
private fun SettingsDialog(
    onClickHideSettingsDialog: () -> Unit,
    velocity: Float,
    onChangeVelocity: (Float) -> Unit,
    size: Float,
    onChangeSize: (Float) -> Unit,
    primaryColor: ColorValue,
    onPrimaryColorChange: (ColorValue) -> Unit,
    backgroundColor: ColorValue,
    onBackgroundColorChange: (ColorValue) -> Unit
) {
    Dialog(onDismissRequest = onClickHideSettingsDialog) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Velocity: $velocity")
                Slider(
                    value = velocity,
                    onValueChange = onChangeVelocity,
                    valueRange = BounceViewModel.velocityRange
                )
                Spacer(Modifier.height(8.dp))
                Text("Size: $size")
                Slider(
                    value = size,
                    onValueChange = onChangeSize,
                    valueRange = BounceViewModel.sizeRange
                )
                Spacer(Modifier.height(8.dp))
                Text("Circle Color")
                Spacer(Modifier.height(4.dp))
                ColorSelection(
                    colors = primaryColors,
                    selectedColor = primaryColor,
                    onClickColor = onPrimaryColorChange
                )
                Spacer(Modifier.height(8.dp))
                Text("Background Color")
                Spacer(Modifier.height(4.dp))
                ColorSelection(
                    colors = backgroundColors,
                    selectedColor = backgroundColor,
                    onClickColor = onBackgroundColorChange
                )
            }
        }
    }
}

@Composable
private fun ColorSelection(
    colors: List<ColorValue>,
    selectedColor: ColorValue?,
    onClickColor: (ColorValue) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(colors) { color ->
            Surface(
                onClick = { onClickColor(color) },
                shape = CircleShape,
                modifier = Modifier.width(24.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    ColorBlob(
                        color = MaterialTheme.colorScheme.onSurface,
                        size = if (color == selectedColor) 20f else 18f
                    )
                    ColorBlob(
                        color = color.color(),
                        size = 16f
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorBlob(color: Color, size: Float = 16f, modifier: Modifier = Modifier) {
    Surface(
        color = color,
        shape = CircleShape,
        modifier = modifier.size(size.dp)
    ) {
        // Empty
    }
}