package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.laziest

class StateNarrationScopeImpl<T>(
    override var uuid: String,
    override val state: MutableState<T>,
    private val enterTransition: EnterTransition? = null,
    private val exitTransition: ExitTransition? = null
) : StateNarrationScope<T, ScopedComposable<StateNarrativeScope>> {

    var isAnimating = false

    var endedAnimation = false

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
    //  private val delegate = JvmNarrationScope(enterTransition, exitTransition, this)

    override val stateSelectors: MutableMap<String, NarrationStateKey<T>> = mutableMapOf()

    val backStack = mutableListOf<String>()

    override val prevKey: String?
        get() = backStack.getOrNull(backStack.size - 2)

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
    }

    /**TODO
     * Not sure how to enable
     * transitions without losing the data that was visible
     * before a state change.
     **/
    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: ScopedComposable<StateNarrativeScope>) = if (enterTransition != null) {
        AnimatedContent(
            composable,
            transitionSpec = {
                enterTransition with (exitTransition ?: fadeOut())
            }
        ) {
            val startingAnimation = !isAnimating
            isAnimating = this.transition.currentState != this.transition.targetState
            endedAnimation = !isAnimating && !startingAnimation
            super.Narrate(composable)
        }
    } else super.Narrate(composable)

    companion object {
        const val TAG = "StateNarrationScopeImpl"
    }

}