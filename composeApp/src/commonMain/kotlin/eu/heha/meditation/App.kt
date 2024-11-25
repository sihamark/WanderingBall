package eu.heha.meditation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val viewModel = remember { MeditationViewModel() }
    MeditationTheme {
        Scaffold {
            var parentWidth by remember { mutableStateOf(0f) }
            val density = LocalDensity.current
            val scope = rememberCoroutineScope()

            var isSettingsButtonVisible by remember { mutableStateOf(false) }
            var hideJob by remember { mutableStateOf<Job?>(null) }

            var duration by remember { mutableStateOf(2000) }

            var isSettingsDialogVisible by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        isSettingsButtonVisible = true
                        hideJob?.cancel()
                        hideJob = scope.launch {
                            delay(2000)
                            isSettingsButtonVisible = false
                        }
                    }
                    .onGloballyPositioned {
                        parentWidth = with(density) { it.size.width.toDp().value }
                    }
            ) {
                val transition = rememberInfiniteTransition(label = "moving")
                LaunchedEffect(duration) {
                    transition.animations.forEach {

                    }
                }
                val offset by transition.animateFloat(
                    initialValue = 0f,
                    targetValue = parentWidth - 50, // the box itself is 50dp wide
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = duration, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterStart)
                        .offset(x = offset.dp)
                ) {
                    // Empty
                }

                AnimatedVisibility(
                    visible = isSettingsButtonVisible,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    IconButton(onClick = { isSettingsDialogVisible = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }

                if (isSettingsDialogVisible) {
                    Dialog(onDismissRequest = { isSettingsDialogVisible = false }) {
                        Surface(
                            modifier = Modifier.padding(16.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text("Duration: ${duration}ms")
                                Slider(
                                    value = duration.toFloat(),
                                    onValueChange = {
                                        duration = it.toInt()
                                    },
                                    valueRange = 2000f..10000f
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}