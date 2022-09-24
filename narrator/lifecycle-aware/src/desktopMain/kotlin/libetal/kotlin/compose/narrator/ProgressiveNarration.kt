package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope


@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit
): Unit = NarrationJvm(prepareNarrations)