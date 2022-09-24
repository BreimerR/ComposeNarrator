package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

@Composable
actual fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: StateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.() -> Unit
): Unit = NarrationJvm(
        state,
        fadeIn(animationSpec = tween(6000)) + slideInVertically(animationSpec = tween(6000)) { it },
        fadeOut(animationSpec = tween(6000)) + slideOutVertically(animationSpec = tween(6000)) { -it },
        prepareNarrations
    )
