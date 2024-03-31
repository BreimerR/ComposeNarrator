package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

/**
 * Having this inside the sharedJvmMain causes errors that we can't
 * handle now due to the compiler majourly
 **/
@Composable
fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
): Unit {

}
