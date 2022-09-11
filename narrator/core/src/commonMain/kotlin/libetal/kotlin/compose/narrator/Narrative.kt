package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationDestination
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

class Narrative<Key : Any>(
    override val narrationScope: ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>,
    private val destination: Key
) : NarrationDestination<Key, ProgressiveNarrativeScope, ScopedComposable<ProgressiveNarrativeScope>> {
    override fun narrate() = with(narrationScope) {
        destination.narrate()
    }
}

