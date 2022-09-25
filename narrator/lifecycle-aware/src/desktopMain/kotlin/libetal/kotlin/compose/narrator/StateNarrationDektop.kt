package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

val defaultAnimationDuration = 5000

val defaultEntryAnimation = fadeIn(animationSpec = tween(defaultAnimationDuration)) + slideInVertically(
    animationSpec = tween(defaultAnimationDuration)
) { it }
val defaultExitAnimation = fadeOut(animationSpec = tween(defaultAnimationDuration)) + slideOutVertically(
    animationSpec = tween(defaultAnimationDuration)
) { -it }

@Composable
actual fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: StateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.() -> Unit
): Unit = NarrationJvm(
    state,
    defaultEntryAnimation,
    defaultExitAnimation,
    prepareNarrations
)
