package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope
import libetal.kotlin.debug.debug
import libetal.kotlin.laziest

typealias StateNarrationComposable<T> = @Composable StateNarrativeScope.(T) -> Unit

class ListBasedStateNarrationScopeImplJvm<T>(
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

    override val stateSelectors by laziest {
        mutableMapOf<String, (T) -> Boolean>()
    }

    override val currentKey: String
        get() {

            for ((hashCode, selector) in stateSelectors) {
                if (selector(state.value)) {
                    return hashCode
                }
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

        super.add(key) {

            content(it)


        }

    }

    @Composable
    override fun Compose(composable: StateNarrationComposable<T>) {
        composable(currentNarrativeScope, state.value)
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate() = when (val composable = composables[currentKey]) {
        null -> Unit
        else -> {

            AnimatedContent(
                state.value,
                transitionSpec = {
                    (enterTransition ?: fadeIn()) with (exitTransition ?: fadeOut())
                }
            ) {

                val startingAnimation = !isAnimating
                isAnimating = this.transition.currentState != this.transition.targetState
                endedAnimation = !isAnimating && !startingAnimation

                val currentComposable =
                    if (endedAnimation) composable else when (val key = prevKey) {
                        null -> composable
                        else -> when (val prevComposable = composables[key]) {
                            null -> composable
                            else -> prevComposable
                        }
                    }

                currentComposable(currentNarrativeScope, state.value)

            }

            DisposableEffect(state) {

                onDispose {

                    val key = prevKey ?: return@onDispose

                    NarrationScope.TAG debug "Disposing $key"

                    cleanUp(key)

                    val listeners = onNarrationEndListeners[key] ?: return@onDispose

                    for (listener in listeners) {
                        listener()
                    }

                }

            }
        }

    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: StateNarrationComposable<T>) = if (enterTransition != null) AnimatedContent(
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
    else super.Narrate(composable)
}