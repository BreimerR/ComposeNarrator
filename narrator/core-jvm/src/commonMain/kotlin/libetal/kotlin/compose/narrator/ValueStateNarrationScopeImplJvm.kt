package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.interfaces.ValueStateNarrationScope

class ValueStateNarrationScopeImplJvm<T>(
    uuid: String,
    state: T,
    val enterTransition: EnterTransition? = fadeIn(),
    val exitTransition: ExitTransition? = fadeOut(),
) : ValueStateNarrationScope<T, StateNarrationComposable<T>>(
    uuid,
    state
) {

    private val String.currentNarrativeScope
        get() = narrativeScopes[this] ?: newNarrativeScope.also {
            narrativeScopes[this] = it
        }

    private val T.key: String
        get() {
            for ((hashCode, selector) in stateSelectors) {
                if (selector(this)) return hashCode

            }

            throw RuntimeException("Failed to resolve a narration. Check your created premises")

        }

    override fun add(key: String, content: StateNarrationComposable<T>) =
        super.add(key) {

            content(it)

            DisposableEffect(key) {
                onDispose {
                    val listeners = onNarrationEndListeners[key] ?: return@onDispose

                    for (listener in listeners) {
                        listener()
                    }
                }
            }
        }

    @Composable
    fun Narrate(state: StateNarrationComposable<T>, key: String, value: T) {
        state(key.currentNarrativeScope, value)
    }

    @Composable
    override fun Narrate() {
        if (enterTransition == null) {
            val currentKey = state.key
            when (val content = composables[currentKey]) {
                null -> println("Failed to load content")
                else -> Narrate(content, currentKey, state)
            }
        } else AnimatedContent(
            state,
            transitionSpec = { enterTransition togetherWith (exitTransition ?: fadeOut()) }
        ) { value ->
            val currentKey = state.key
            when (val content = composables[currentKey]) {
                null -> println("Failed to load content")
                else -> Narrate(content, currentKey, value)
            }
        }
    }

    companion object {
        const val TAG = "ListBasedStateNarrationScopeImplJvm"
    }

}
