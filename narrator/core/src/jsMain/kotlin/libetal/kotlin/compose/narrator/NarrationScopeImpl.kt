package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

/*
class NarrationScopeImpl<Key : Any> constructor(
    val backStack: ListBackStack<Key>
) : ProgressiveNarrationScope<Key, @Composable () -> Unit> {
    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty

    override val currentComponent
        get() = composables[currentKey]
    override val currentNarrativeScope: NarrativeScope
        get() = TODO("Not yet implemented")

    override val composables: MutableMap<Key, @Composable () -> Unit> by laziest {
        mutableMapOf()
    }

    override val narrativeScopes: MutableMap<Key, NarrativeScope> by laziest { mutableMapOf() }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest { mutableListOf() }

    override val children: MutableList<NarrationScope<Key, @Composable () -> Unit>> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, () -> Unit>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    @Composable
    override fun Narrate(composable: @Composable () -> Unit) = composable()

}*/
