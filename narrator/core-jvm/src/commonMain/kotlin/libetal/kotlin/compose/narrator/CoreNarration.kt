package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope

/**
 * @param backHandler
 * If provided handles
 **/
@Composable
fun <Key : Any> Narration(prepareComponents: NarrationScopeImpl<Key>.() -> Unit) =
    remember { mutableStateListOf<Key>() }.let { stack ->
        Narration(
            { uuid ->
                NarrationScopeImpl(
                    uuid,
                    NarrationBackStack(
                        stack
                    )
                )
            },
            prepareComponents
        )
    }

@Composable
fun <T> Narration(
    state: MutableState<T>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    prepareNarrations: StateNarrationScope<T, ScopedComposable<StateNarrativeScope>>.() -> Unit
) = StateNarrationScopeImpl(
    uuid = "${prepareNarrations.hashCode()}",
    state = remember { state },
    enterTransition = enterTransition,
    exitTransition = exitTransition
) Narration prepareNarrations