package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.laziest

typealias StateNarrationComposable<T> = @Composable StateNarrativeScope.(T) -> Unit

class StateNarrationScopeImplJvm<T>(
    override val uuid: String,
    val stack: SnapshotStateList<String>,
    override val state: MutableState<T>,
    val enterTransition: EnterTransition? = fadeIn(),
    val exitTransition: ExitTransition? = fadeOut()
) : StateNarrationScope<T, StateNarrationComposable<T>> {

    private var isAnimating = false
    private var endedAnimation = false

    private val backStack by laziest {
        NarrationBackStack(
            stack
        )
    }

    private val String.currentNarrativeScope
        get() = narrativeScopes[this] ?: newNarrativeScope.also {
            narrativeScopes[this] = it
        }

    override val stateSelectors by laziest {
        mutableMapOf<String, (T) -> Boolean>()
    }


    private val T.key: String
        get() {
            for ((hashCode, selector) in stateSelectors) {
                if (selector(this)) return hashCode

            }

            throw RuntimeException("Failed")

        }

    override val prevKey: String?
        get() = backStack.previous

    override val shouldExit: Boolean
        get() = backStack.isEmpty

    override val newNarrativeScope: StateNarrativeScope
        get() = StateNarrativeScope()

    override val composables by laziest {
        mutableMapOf<String, StateNarrationComposable<T>>()
    }

    override val narrativeScopes by laziest {
        mutableMapOf<String, StateNarrativeScope>()
    }

    override val onNarrationEndListeners by laziest {
        mutableMapOf<String, MutableList<() -> Unit>>()
    }

    override val children by laziest {
        mutableMapOf<String, NarrationScope<out Any, out NarrativeScope, StateNarrationComposable<T>>>()
    }

    override val onNarrativeExitRequest by laziest {
        mutableMapOf<String, MutableList<(NarrationScope<String, StateNarrativeScope, StateNarrationComposable<T>>) -> Boolean>?>()
    }

    override fun add(key: String, content: StateNarrationComposable<T>) {

        if (backStack.isEmpty && (stateSelectors[key]?.invoke(state.value) == true)) backStack.add(key)

        super.add(key, content)

    }


    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate() = AnimatedContent(
        currentValue,
        transitionSpec = { (enterTransition ?: fadeIn()) with (exitTransition ?: fadeOut()) }
    ) { value ->

        val currentKey = value.key

        when (val composable = composables[currentKey]) {
            null -> Unit
            else -> {

                val startingAnimation = !isAnimating
                isAnimating = this.transition.currentState != this.transition.targetState
                endedAnimation = !isAnimating && !startingAnimation

                val currentComposable = when (val key = prevKey) {
                    null -> composable
                    else -> when (val prevComposable = composables[key]) {
                        null -> composable
                        else -> prevComposable
                    }
                }

                currentComposable(currentKey.currentNarrativeScope, value)

            }
        }

    }

    companion object {
        const val TAG = "ListBasedStateNarrationScopeImplJvm"
    }

}