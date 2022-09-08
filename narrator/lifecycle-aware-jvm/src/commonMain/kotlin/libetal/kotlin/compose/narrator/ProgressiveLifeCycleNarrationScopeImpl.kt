package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.laziest

class ProgressiveLifeCycleNarrationScopeImpl<Key : Any>(
    override val backStack: ListBackStack<Key>,
    private val enterTransition: EnterTransition? = null,
    private val exitTransition: ExitTransition? = null
) : ProgressiveLifeCycleNarrationScope<Key, ComposableFun> {

    var isAnimating = false

    override val Key.key: Key
        get() = this

    override val composables: MutableMap<Key, ComposableFun> by laziest {
        mutableMapOf()
    }

    override val narrativeScopes: MutableMap<Key, NarrativeScope> by laziest {
        mutableMapOf()
    }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest {
        mutableListOf()
    }

    override val children: MutableList<NarrationScope<Key, Key, ComposableFun>> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Key, ComposableFun>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: ComposableFun) = if (enterTransition != null) {
        val exitTransition = exitTransition ?: fadeOut()
        AnimatedContent(
            composable,
            transitionSpec = {
                enterTransition with exitTransition
            },
            contentAlignment = Alignment.CenterEnd
        ) {
            //TODO I think the end of this animation is denoted when startAnimating = false && isAnimating = false
            val startingAnimation = !isAnimating
            isAnimating = this.transition.currentState != this.transition.targetState
            val ended = !isAnimating && !startingAnimation
            currentNarrativeScope.it()
        }
    } else {
        currentNarrativeScope.composable()
    }

}