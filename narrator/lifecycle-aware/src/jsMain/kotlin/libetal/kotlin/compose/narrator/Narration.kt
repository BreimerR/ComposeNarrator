package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope


@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit
){
    TODO("Pending Implementation")
}
