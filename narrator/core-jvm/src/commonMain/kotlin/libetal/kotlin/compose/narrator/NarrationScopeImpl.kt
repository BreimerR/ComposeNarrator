package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

class NarrationScopeImpl<Key : Any> constructor(
    override var uuid: String,
    override val backStack: ListBackStack<Key>,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
) : ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>> {

    private val delegate = JvmNarrationScope(enterTransition, exitTransition, this)

    constructor(uuid: String, backStack: ListBackStack<Key>) : this(uuid, backStack, fadeIn(), fadeOut())

    override fun add(key: Key, content: ScopedComposable<ProgressiveNarrativeScope>) = super.add(key) {
        content()
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

    //@Composable
    //override fun Narrate(composable: ScopedComposable<ProgressiveNarrativeScope>) = delegate.Narrate(composable)

    @Composable
    override fun Compose(composable: ScopedComposable<ProgressiveNarrativeScope>) {
        composable(currentNarrativeScope)
    }

}

