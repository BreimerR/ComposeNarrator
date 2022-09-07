package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

class NarrationScopeImpl<Key : Any> constructor(
    backStack: ListBackStack<Key>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
) : ProgressiveNarrationScope<Key, ComposableFun> {

    private val delegate = JvmNarrationScope(backStack, enterTransition, exitTransition, this)

    constructor(backStack: ListBackStack<Key>) : this(backStack, fadeIn(), fadeOut())

    override fun add(key: Key, content: ComposableFun) = super<ProgressiveNarrationScope>.add(key) {
        content()
    }

    override val backStack: ListBackStack<Key>
        get() = delegate.backStack
    override val shouldExit: Boolean
        get() = delegate.shouldExit
    override val composables: MutableMap<Key, ComposableFun>
        get() = delegate.composables
    override val narrativeScopes: MutableMap<Key, NarrativeScope>
        get() = delegate.narrativeScopes
    override val onNarrationEndListeners: MutableList<() -> Unit>
        get() = delegate.onNarrationEndListeners
    override val children: MutableList<NarrationScope<Key, ComposableFun>>
        get() = delegate.children
    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ComposableFun>) -> Boolean>?>
        get() = delegate.onNarrativeExitRequest

    @Composable
    override fun Narrate(composable: ComposableFun) = delegate.Narrate(composable)

}

