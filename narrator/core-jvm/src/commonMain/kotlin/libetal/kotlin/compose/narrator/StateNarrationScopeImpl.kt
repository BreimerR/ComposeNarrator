package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.laziest

class StateNarrationScopeImpl<Key>(
    override val state: MutableState<Key>, backStack: ListBackStack<Int>,
    enterTransition: EnterTransition? = null, exitTransition: ExitTransition? = null
) : StateNarrationScope<Key, ComposableFun> {

    private val delegate = JvmNarrationScope(backStack, enterTransition, exitTransition, this)

    override val stateSelectors: MutableMap<Int, NarrationStateKey<Key>> = mutableMapOf()

    override val currentKey: Int
        get() {

            for ((hashCode, test) in stateSelectors) {
                if (test(state.value)) return hashCode
            }

            throw RuntimeException("Failed to get a runner for this state")
        }

    override val shouldExit: Boolean
        get() = delegate.shouldExit

    override val composables: MutableMap<Int, ComposableFun>
        get() = delegate.composables

    override val narrativeScopes: MutableMap<Int, NarrativeScope>
        get() = delegate.narrativeScopes

    override val onNarrationEndListeners: MutableList<() -> Unit>
        get() = delegate.onNarrationEndListeners

    override val children: MutableList<NarrationScope<Int, ComposableFun>>
        get() = delegate.children

    override val onNarrativeExitRequest: MutableMap<Int, MutableList<(NarrationScope<Int, ComposableFun>) -> Boolean>?>
        get() = delegate.onNarrativeExitRequest

    @Composable
    override fun Narrate(composable: ComposableFun) = delegate.Narrate(composable)

}