package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Suppress("UNCHECKED_CAST")
val <Key : Any> Key.narrative
    @Composable get() = Narrative(
        LocalNarrationScope.current as? ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>
            ?: throw RuntimeException("Failed to retrieve proper narration"),
        this
    )

val narrationScope
    @Composable get() = LocalNarrationScope.current ?: throw RuntimeException("Method should be called inside a Narration. Or a composable nesetd inside a Narration")

@Composable
infix fun <Key : Any, N : NarrationScope<Key, *, *>> N.Narration(
    prepareNarratives: N.() -> Unit
) {
    LocalNarrationScope.current?.addChild(this)
    for (collector in scopeCollectors) {
        collector collect this
    }
    scopeCollectors.clear()
    CompositionLocalProvider(LocalNarrationScope provides this) {
        prepareNarratives()
        Narrate()
    }

}

@Composable
fun <Key : Any, N : NarrationScope<Key, *, *>> Narration(
    scopeBuilder: (uuid: String) -> N,
    prepareNarratives: N.() -> Unit
) {
    val scope = scopeBuilder(
        "${prepareNarratives.hashCode()}"
    )

    scope Narration prepareNarratives

}