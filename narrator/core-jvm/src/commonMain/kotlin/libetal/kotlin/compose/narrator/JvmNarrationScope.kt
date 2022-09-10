package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.laziest

class JvmNarrationScope<Key : Any, Scope : NarrativeScope, Content>(
    private val enterTransition: EnterTransition?,
    private val exitTransition: ExitTransition?,
    delegate: NarrationScope<Key, Scope, Content>,
    private val composer: @Composable Scope.(Content, start: Boolean, end: Boolean) -> Unit
) : NarrationScope<Key, Scope, Content> by delegate {

    var isAnimating: Boolean = false

    override val narrativeScopes by laziest {
        mutableMapOf<Key, Scope>()
    }

    override val composables: MutableMap<Key, Content> by laziest {
        mutableMapOf()
    }

    override val children: MutableList<NarrationScope<Key, Scope, Content>> by laziest {
        mutableListOf()
    }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Scope, Content>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: Content) = if (enterTransition != null) {
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
            currentNarrativeScope.composer(it, startingAnimation, !isAnimating && !startingAnimation)
        }
    } else {
        currentNarrativeScope.composer(composable, false, true)
    }

}