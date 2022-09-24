package libetal.kotlin.compose.narrator

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

@Composable
actual fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: StateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.() -> Unit
): Unit = NarrationJvm(state, fadeIn(), fadeOut(), prepareNarrations)