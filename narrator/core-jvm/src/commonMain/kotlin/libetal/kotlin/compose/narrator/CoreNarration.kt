package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

/**
 * @param backHandler
 * If provided handles
 **/
@Composable
fun <Key : Any> Narration(prepareComponents: @Composable ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit) {
    val backStack = NarrationBackStack(
        remember { mutableStateListOf<Key>() }
    )
    val scope = NarrationScopeImpl(backStack)
    for (collector in scopeCollectors) {
        collector collect scope
    }
    scopeCollectors.clear()
    Narration(scope, prepareComponents)
}

@Composable
fun <T> Narration(
    state: MutableState<T>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    prepareNarrations: StateNarrationScope<T, StateComposable<T>>.() -> Unit
) {
    val scope = StateNarrationScopeImpl(
        state,
        enterTransition,
        exitTransition
    )

    for (collector in scopeCollectors) {
        collector collect scope
    }
    scopeCollectors.clear()
    CompositionLocalProvider(LocalNarrationScope provides scope) {
        with(scope) {
            prepareNarrations()
            Narrate()
        }
    }
}

