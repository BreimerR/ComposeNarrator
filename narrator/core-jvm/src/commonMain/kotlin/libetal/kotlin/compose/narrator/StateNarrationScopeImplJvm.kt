/*
package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.laziest

class StateNarrationScopeImplJvm<T>(
    override var uuid: String,
    override val state: MutableState<T>,
    private val enterTransition: EnterTransition?,
    private val exitTransition: ExitTransition?
) : StateNarrationScope<T, ScopedComposable<StateNarrativeScope>> {

    var isAnimating = false

    var endedAnimation = false

    override val stateSelectors: MutableMap<String, NarrationStateKey<T>> = mutableMapOf()

    val backStack = mutableListOf<String>()

    override val prevKey: String?
        get() = backStack.getOrNull(backStack.size - 2)

    val stateValues = mutableMapOf<String, T>()

    override val currentKey: String
        get() {

            for ((hashCode, test) in stateSelectors) {
                if (test(state.value)) {

                    if (backStack.lastOrNull() != hashCode) {
                        if (hashCode in backStack) backStack.remove(hashCode)
                        backStack.add(hashCode)
                    }
                    return hashCode
                }
            }

            throw RuntimeException("Missing composables: Probably No premise functions were invoked in StateNarration")
        }

    override val shouldExit: Boolean
        get() = false

    override val composables by laziest {
        mutableMapOf<String, ScopedComposable<StateNarrativeScope>>()
    }

    override val narrativeScopes by laziest {
        mutableMapOf<String, StateNarrativeScope>()
    }

    override val onNarrationEndListeners by laziest {
        mutableMapOf<String, MutableList<() -> Unit>>()
    }

    override val children by laziest {
        mutableMapOf<String, NarrationScope<out Any, out NarrativeScope, ScopedComposable<StateNarrativeScope>>>()
    }

    override val onNarrativeExitRequest by laziest {
        mutableMapOf<String, MutableList<(NarrationScope<String, StateNarrativeScope, ScopedComposable<StateNarrativeScope>>) -> Boolean>?>()
    }

    @Composable
    override fun Compose(composable: ScopedComposable<StateNarrativeScope>) {
        composable(currentNarrativeScope)
    }

    override fun cleanUp(key: String) {
        super.cleanUp(key)
        backStack.remove(key)

        prevKey?.let {
            stateValues.remove(it)
        }

    }

    */
/**TODO
     * Not sure how to enable
     * transitions without losing the data that was visible
     * before a state change.
     **//*



    companion object {
        const val TAG = "StateNarrationScopeImpl"
    }

}*/
