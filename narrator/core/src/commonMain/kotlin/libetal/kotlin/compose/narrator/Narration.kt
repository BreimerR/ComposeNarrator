package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Suppress("UNCHECKED_CAST")
val <Key: Any> Key.narrative
    @Composable get() = Narrative(
        LocalNarrationScope.current as? ProgressiveNarrationScope<Key, @Composable () -> Unit>
            ?: throw RuntimeException("Failed to retrieve proper narration"),
        this
    )

@Composable
fun <Key : Any> Narration(
    scope: ProgressiveNarrationScope<Key,@Composable NarrativeScope.() -> Unit>,
    prepareNarratives: @Composable ProgressiveNarrationScope<Key, @Composable NarrativeScope.() -> Unit>.() -> Unit
) = CompositionLocalProvider(LocalNarrationScope provides scope) {
    with(scope) {
        prepareNarratives()
        Narrate()
    }
}