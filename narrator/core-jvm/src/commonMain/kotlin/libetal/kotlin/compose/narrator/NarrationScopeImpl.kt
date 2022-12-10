package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.log.debug

class NarrationScopeImpl<Key : Any> constructor(
    override var uuid: String,
    override val backStack: ListBackStack<Key>,
    val enterTransition: EnterTransition? = null,
    val exitTransition: ExitTransition? = null,
) : ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>> {

    private val delegate = JvmNarrationScope(enterTransition, exitTransition, this)

    constructor(uuid: String, backStack: ListBackStack<Key>) : this(uuid, backStack, fadeIn(), fadeOut())

    override fun add(key: Key, content: ScopedComposable<ProgressiveNarrativeScope>) = super.add(key) {

        content()

        DisposableEffect(backStack) {

            onDispose {

                val listeners = onNarrationEndListeners[key] ?: return@onDispose

                for (listener in listeners) {
                    listener()
                }

                cleanUp(key)

            }

        }

    }

    override val composables: MutableMap<Key, ScopedComposable<ProgressiveNarrativeScope>>
        get() = delegate.composables

    override val narrativeScopes: MutableMap<Key, ProgressiveNarrativeScope>
        get() = delegate.narrativeScopes

    override val onNarrationEndListeners
        get() = delegate.onNarrationEndListeners

    override val children
        get() = delegate.children

    override val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ProgressiveNarrativeScope, ScopedComposable<ProgressiveNarrativeScope>>) -> Boolean>?>
        get() = delegate.onNarrativeExitRequest

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate() {

        val exitTransition = exitTransition ?: fadeOut()

        val enterTransition = enterTransition ?: fadeIn()

        AnimatedContent(
            currentKey,
            transitionSpec = {
                enterTransition with exitTransition
            }
        ) {

            val composable = composables[it]

            if (composable != null) {
                composable(it.currentNarrativeScope)
            }

        }

    }

}

