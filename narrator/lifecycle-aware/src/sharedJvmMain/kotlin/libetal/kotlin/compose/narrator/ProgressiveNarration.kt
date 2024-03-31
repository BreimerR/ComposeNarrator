package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel

@Composable
expect fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
)


@Composable
expect inline fun <T : Any, reified VM : ViewModel> Narration(
   noinline vmFactory: () -> VM,
   noinline  prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
): Unit
