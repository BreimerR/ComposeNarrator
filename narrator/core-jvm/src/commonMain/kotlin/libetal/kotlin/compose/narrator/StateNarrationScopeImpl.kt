package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.debug.info
import libetal.kotlin.laziest

class StateNarrationScopeImpl<T>(
    override var uuid: String,
    override val state: MutableState<T>,
    private val enterTransition: EnterTransition? = null,
    private val exitTransition: ExitTransition? = null
) : StateNarrationScope<T, ScopedComposable<StateNarrativeScope>> {

    var isAnimating = false
    var endedAnimation = false
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
    //  private val delegate = JvmNarrationScope(enterTransition, exitTransition, this)

    override val stateSelectors: MutableMap<Int, NarrationStateKey<T>> = mutableMapOf()

    override val currentKey: Int
        get() {

            for ((hashCode, test) in stateSelectors) {
                if (test(state.value)) return hashCode
            }

            throw RuntimeException("Missing composables: Probably No premise functions were invoked in StateNarration")
        }

    override val shouldExit: Boolean
        get() = false

    override val composables by laziest {
        mutableMapOf<Int, ScopedComposable<StateNarrativeScope>>()
    }

    override val narrativeScopes by laziest {
        mutableMapOf<Int, StateNarrativeScope>()
    }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest {
        mutableListOf()
    }

    override val children by laziest {
        mutableMapOf<String, NarrationScope<out Any, out NarrativeScope, ScopedComposable<StateNarrativeScope>>>()
    }

    override val onNarrativeExitRequest by laziest {
        mutableMapOf<Int, MutableList<(NarrationScope<Int, StateNarrativeScope, ScopedComposable<StateNarrativeScope>>) -> Boolean>?>()
    }

    @Composable
    override fun Compose(composable: ScopedComposable<StateNarrativeScope>) {
        composable(currentNarrativeScope)
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: ScopedComposable<StateNarrativeScope>) = if (enterTransition != null) {
        val exitTransition = exitTransition ?: fadeOut()
        AnimatedContent(
            composable,
            transitionSpec = {
                enterTransition with exitTransition
            }
        ) {
            // TODO I think the end of this animation is denoted when startAnimating = false && isAnimating = false
            val startingAnimation = !isAnimating
            isAnimating = this.transition.currentState != this.transition.targetState
            endedAnimation = !isAnimating && !startingAnimation
            composable(currentNarrativeScope)
        }
    } else {
        composable(currentNarrativeScope)
    }

    companion object {
        const val TAG = "StateNarrationScopeImpl"
    }

}