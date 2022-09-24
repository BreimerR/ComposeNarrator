package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope


@Composable
expect fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: StateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.() -> Unit
)