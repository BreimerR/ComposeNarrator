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
fun <Key : Any> Narration(prepareComponents: @Composable NarrationScopeImpl<Key>.() -> Unit) {
    val backStack = NarrationBackStack(
        remember { mutableStateListOf<Key>() }
    )
    val current = LocalNarrationScope.current
    val scope = NarrationScopeImpl(
        "${prepareComponents.hashCode()}",
        backStack
    )
    current?.add(scope)
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
    prepareNarrations: @Composable StateNarrationScope<T, ScopedComposable<StateNarrativeScope>>.() -> Unit
) {

    val current = LocalNarrationScope.current

    val scope = StateNarrationScopeImpl(
        "${prepareNarrations.hashCode()}",
        remember { state },
        enterTransition,
        exitTransition
    )

    current?.add(scope)

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

