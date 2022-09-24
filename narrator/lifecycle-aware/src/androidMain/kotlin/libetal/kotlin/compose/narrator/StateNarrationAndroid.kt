package libetal.kotlin.compose.narrator

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
actual fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: StateNarrationScope<T, ScopedComposable<StateNarrativeScope>>.() -> Unit
): Unit = NarrationJvm(state, fadeIn(), fadeOut(), prepareNarrations)