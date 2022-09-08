package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

interface ProgressiveLifeCycleNarrationScope<Key : Any, C> : ProgressiveNarrationScope<Key, C> {
    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty
}