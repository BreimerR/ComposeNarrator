package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope


@Composable
fun <T> NarrationJvm(
    state: MutableState<T>,
    enterTransition: EnterTransition? = fadeIn(),
    exitTransition: ExitTransition? = fadeOut(),
    prepareNarrations: StateNarrationScope<T, ScopedComposable<StateNarrativeScope>>.() -> Unit
) = Narration(
    { uuid ->
        StateNarrationScopeImpl(
            uuid = uuid,
            state = state,
            enterTransition = enterTransition,
            exitTransition = exitTransition
        )
    },
    prepareNarrations
)
