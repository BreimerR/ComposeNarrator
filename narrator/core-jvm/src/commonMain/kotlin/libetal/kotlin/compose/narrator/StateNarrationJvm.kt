package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope


@Composable
fun <T> NarrationJvm(
    state: MutableState<T>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    prepareNarrations: StateNarrationScope<T, ScopedComposable<StateNarrativeScope>>.() -> Unit
) = remember { state }.let { rememberedState ->
    Narration(
        { uuid ->
            StateNarrationScopeImpl(
                uuid = uuid,
                state = rememberedState,
                enterTransition = enterTransition,
                exitTransition = exitTransition
            )
        },
        prepareNarrations
    )
}