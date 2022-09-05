package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

class ProgressiveNarrativeScope<K : Any, N : ProgressiveNarrationScope<K, *>>(private val narrationScope: N) :
    NarrativeScope() {
    fun narrate(key: K) = narrationScope.backStack.navigateTo(key)
}

