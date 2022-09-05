package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

class NarrationScopeImpl<Key> constructor(
    override val backStack: ListBackStack<Key>
) : ProgressiveNarrationScope<Key, @Composable () -> Unit> {

    override val currentComponent
        get() = composables[currentKey]

    override val composables: MutableMap<Key, @Composable () -> Unit> by laziest {
        mutableMapOf()
    }

    override val children: MutableList<NarrationScope<Key, @Composable () -> Unit>> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, (NarrationScope<Key, @Composable () -> Unit>) -> Boolean> by laziest {
        mutableMapOf()
    }

    override val onExitRequestListeners: MutableList<ExitRequestListener> by laziest {
        mutableListOf()
    }

    @Composable
    override fun Narrate(composable: @Composable () -> Unit) = composable()

}