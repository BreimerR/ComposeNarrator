package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.laziest

class JvmNarrationScope<Key : Any, Scope : NarrativeScope, Content>(
    private val enterTransition: EnterTransition?,
    private val exitTransition: ExitTransition?,
    delegate: NarrationScope<Key, Scope, Content>
) : NarrationScope<Key, Scope, Content> by delegate {

    var isAnimating: Boolean = false

    var endedAnimation = false

    override val narrativeScopes by laziest {
        mutableMapOf<Key, Scope>()
    }

    override val composables: MutableMap<Key, Content> by laziest {
        mutableMapOf()
    }


    override val children: MutableMap<String, NarrationScope<out Any, out NarrativeScope, Content>> by laziest {
        mutableMapOf()
    }

    override val onNarrationEndListeners by laziest {
        mutableMapOf<Key, MutableList<() -> Unit>>()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Scope, Content>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: Content) = if (enterTransition != null) AnimatedContent(
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