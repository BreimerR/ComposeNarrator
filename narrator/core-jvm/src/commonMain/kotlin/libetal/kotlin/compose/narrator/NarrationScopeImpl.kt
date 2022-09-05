package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.listeners.BackPressListener
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

typealias ComposableFun = @Composable ()->Unit
typealias NarrativeComposable = @Composable NarrativeScope.() -> Unit

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

    infix fun Key.narrates(content: @Composable NarrativeScope.() -> Unit) = add(content)

    /*TODO: IRLowering fails if this is not there*/
    private fun Key.add(content: @Composable NarrativeScope.() -> Unit) {
        addToBackstack()
        composables[this] = {

            val scope = NarrativeScope()

            @Suppress("UNCHECKED_CAST") // TODO Might need to throw but it's not needed now
            val narrationScope = LocalNarrationScope.current as? NarrationScope<Key, @Composable () -> Unit>

            content(scope)

            if (scope.hasExitListeners) (LocalNarrationScope.current as? NarrationScope<Key, ComposableFun>)?.apply {
                onNarrativeExitRequest[this@add] = {
                    scope.back()
                }
            }


        }
    }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate(composable: @Composable () -> Unit) {
        if (enterTransition != null) {
            val exitTransition = exitTransition ?: fadeOut()
            AnimatedContent<@Composable () -> Unit>(
                composable,
                transitionSpec = {
                    enterTransition with exitTransition
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                it()
            }
        } else {
            composable()
        }
    }

}