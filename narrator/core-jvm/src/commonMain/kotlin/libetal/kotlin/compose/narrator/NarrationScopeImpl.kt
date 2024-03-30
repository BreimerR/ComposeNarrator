package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

class NarrationScopeImpl<Key : Any>(
    uuid: String,
    backStack: ListBackStack<Key>,
    val enterTransition: EnterTransition? = null,
    val exitTransition: ExitTransition? = null,
) : ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>(
    uuid,
    backStack
) {

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

    override val currentKey
        get() = super.currentKey

    override val Key.currentNarrativeScope: ProgressiveNarrativeScope
        get() = narrativeScopes[this] ?: newNarrativeScope.also {
            narrativeScopes[this] = it
        }


    @Composable
    override fun Narrate() {

        val lambdaExitTransition = exitTransition ?: fadeOut()

        val lambdaEnterTransition = enterTransition ?: fadeIn()

        AnimatedContent(
            currentKey,
            transitionSpec = {
                lambdaEnterTransition togetherWith lambdaExitTransition
            }
        ) {

            val composable = composables[it]

            if (composable != null) {
                composable(it.currentNarrativeScope)
            }

        }

    }

}
