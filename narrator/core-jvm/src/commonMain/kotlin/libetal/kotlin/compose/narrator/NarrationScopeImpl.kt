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

typealias ComposableFun = @Composable NarrativeScope.() -> Unit
// typealias NarrativeComposable = @Composable ProgressiveNarrativeScope.() -> Unit

class NarrationScopeImpl<Key> constructor(
    override val backStack: ListBackStack<Key>,
    private val enterTransition: EnterTransition? = null,
    private val exitTransition: ExitTransition? = null
) : ProgressiveNarrationScope<Key, ComposableFun> {

    override val currentComponent
        get() = composables[currentKey]

    override val composables: MutableMap<Key, ComposableFun> by laziest {
        mutableMapOf()
    }

    override val children: MutableList<NarrationScope<Key, ComposableFun>> by laziest {
        mutableListOf()
    }

    override val onNarrativeExitRequest: MutableMap<Key, (NarrationScope<Key, ComposableFun>) -> Boolean> by laziest {
        mutableMapOf()
    }

    override val onExitRequestListeners: MutableList<ExitRequestListener> by laziest {
        mutableListOf()
    }

    constructor(backStack: ListBackStack<Key>) : this(backStack, fadeIn(), fadeOut())

    /*TODO: IRLowering fails if this is not there*/
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
                it(ProgressiveNarrativeScope())
            }
        } else {
            composable(ProgressiveNarrativeScope())
        }
    }

}