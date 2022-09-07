package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.laziest

open class JvmNarrationScope<Key : Any>(
    val backStack: ListBackStack<Key>,
    private val enterTransition: EnterTransition?,
    private val exitTransition: ExitTransition?,
    delegate:NarrationScope<Key,ComposableFun>
) : NarrationScope<Key, ComposableFun>  by delegate{

    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty

    override val narrativeScopes by laziest {
        mutableMapOf<Key, NarrativeScope>()
    }

    override val composables: MutableMap<Key, ComposableFun> by laziest {
        mutableMapOf()
    }

    override val children: MutableList<NarrationScope<Key, ComposableFun>> by laziest {
        mutableListOf()
    }

    override val onNarrationEndListeners: MutableList<() -> Unit> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ComposableFun>) -> Boolean>?> by laziest {
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
            it(currentNarrativeScope)
        }
    } else {
        composable(currentNarrativeScope)
    }

}