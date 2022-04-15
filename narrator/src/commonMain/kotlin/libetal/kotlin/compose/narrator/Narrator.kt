package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*

@Composable
fun <Key> Narrator(
    enterTransition: EnterTransition = defaultEntryAnimation,
    exitTransition: ExitTransition = defaultExitAnimation,
    content: @Composable NarratorScope<Key>.() -> Unit
) {
    val narrations = remember { mutableStateListOf<Key>() }

    val scope = NarrationScope(
        NarrationBackStack(narrations),
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        content(NarratorScope())
    }

}

@Composable
fun <T> Narrator(
    state: MutableState<T>,
    exitState: T,
    content: @Composable StateNarrationScope<T>.() -> Unit
) {

    val currentState = remember { state }

    val narrations = remember { mutableStateListOf<StateNarrationKey<T>>() }
    val scope = StateNarrationScope(StateNarrationBackStack(narrations), currentState, exitState)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        content(scope)
    }

}
