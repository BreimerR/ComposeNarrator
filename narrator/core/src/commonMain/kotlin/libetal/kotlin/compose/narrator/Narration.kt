package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Suppress("UNCHECKED_CAST")
val <Key : Any> Key.narrative
    @Composable get() = Narrative(
        LocalNarrationScope.current as? ProgressiveNarrationScope<Key, @Composable ProgressiveNarrativeScope.() -> Unit>
            ?: throw RuntimeException("Failed to retrieve proper narration"),
        this
    )


val <Key : Any> Key.isInProgress
    @Composable get() : Boolean = TODO("Checks if the current narration key is the active one or not. For state narrations we check if a certain state is active.")

val narrationScope
    @Composable get() = LocalNarrationScope.current
        ?: throw RuntimeException("Method should be called inside a Narration. Or a composable nesetd inside a Narration")


// Key : Any, Scope : NarrativeScope, Content
@Composable
infix fun <Key : Any, N : NarrationScope<Key, *, *>> N.Narration(
    prepareNarratives: N.() -> Unit
) {
    LocalNarrationScope.current?.addChild(this)
    for (collector in scopeCollectors) {
        collector collect this
    }
    scopeCollectors.clear()

    prepareNarratives()

    CompositionLocalProvider(LocalNarrationScope provides this) {
        Narrate()
    }

}

@Composable
fun <Key : Any, N : NarrationScope<Key, *, *>> Narration(
    scopeBuilder: (uuid: String) -> N,
    prepareNarratives: N.() -> Unit
) {

    val uuid = prepareNarratives.hashCode()

    val scope = scopeBuilder(
        "$uuid"
    )

    scope Narration prepareNarratives

}
