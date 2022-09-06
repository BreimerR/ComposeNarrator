package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

class NarrationScopeImpl<Key : Any> constructor(
    override val backStack: ListBackStack<Key>,
    private val enterTransition: EnterTransition? = null,
    private val exitTransition: ExitTransition? = null
) : ProgressiveNarrationScope<Key, ComposableFun> {

    override val narrativeScopes by laziest {
        mutableMapOf<Key, NarrativeScope>()
    }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest {
        mutableListOf()
    }

    override fun createNarrative() =
        ProgressiveNarrativeScope(this@NarrationScopeImpl)

    override val composables by laziest {
        mutableMapOf<Key, ComposableFun>()
    }

    override val children: MutableList<NarrationScope<Key, ComposableFun>> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ComposableFun>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    constructor(backStack: ListBackStack<Key>) : this(backStack, fadeIn(), fadeOut())

    override fun add(key: Key, content: ComposableFun) = super.add(key) {
        content()
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: ComposableFun) {
        if (enterTransition != null) {
            val exitTransition = exitTransition ?: fadeOut()
            AnimatedContent(
                composable,
                transitionSpec = {
                    enterTransition with exitTransition
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                it(currentNarrativeScope)
            }
        } else {
            composable(currentNarrativeScope)
        }
    }

}