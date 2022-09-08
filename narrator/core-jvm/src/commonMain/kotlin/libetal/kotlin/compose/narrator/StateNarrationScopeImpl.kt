package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.debug.info

class StateNarrationScopeImpl<T>(
    override val state: MutableState<T>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null
) : StateNarrationScope<T, StateComposable<T>> {

    val currentSelector
        get() = stateSelectors[currentKey] ?: throw RuntimeException("Kotlin Error should not be null unless wrongly cleared")

    /**
     * Can't save the old value of the state
     * to utilize during animation.
     * This might cause errors in the case of null states
     * i.e
     * ```
     * val userState = remember{ mutableStateOf<User?>(null) }
     * Narration(userState){
     *
     *     isNull{
     *
     *     }
     *
     *     isNotNull {
     *         // The test is 100% sure not supposed to be null.
     *         // Can't observe state can't provide the 100% certainty
     *         Text("User Name: ${userState.value!!}") This will crash the application if used
     *         Button({
     *            userState.value = null
     *         }){
     *             Text("Clears the state"))
     *         }
     *     }
     * }
     * ```
     **/
    private val delegate = JvmNarrationScope(null, null, this) { composable, starting, ended ->
        composable(state.value)
    }

    override val stateSelectors: MutableMap<Int, NarrationStateKey<T>> = mutableMapOf()

    override val currentKey: Int
        get() {

            for ((hashCode, test) in stateSelectors) {
                if (test(state.value)) return hashCode
            }

            throw RuntimeException("Failed to get a runner for this state ${state.value}")
        }

    override val shouldExit: Boolean
        get() = true

    override val composables: MutableMap<Int, StateComposable<T>>
        get() = delegate.composables

    override val narrativeScopes
        get() = delegate.narrativeScopes

    override val onNarrationEndListeners: MutableList<() -> Unit>
        get() = delegate.onNarrationEndListeners

    override val children
        get() = delegate.children

    override val onNarrativeExitRequest
        get() = delegate.onNarrativeExitRequest

    @Composable
    override fun Narrate(composable: StateComposable<T>) = delegate.Narrate(composable)

    companion object {
        const val TAG = "StateNarrationScopeImpl"
    }

}