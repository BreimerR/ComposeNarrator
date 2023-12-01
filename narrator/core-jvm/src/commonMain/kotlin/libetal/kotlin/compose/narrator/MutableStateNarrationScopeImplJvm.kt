package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.MutableStateNarrationScope
import libetal.kotlin.laziest


class MutableStateNarrationScopeImplJvm<T>(
    uuid: String,
    state: MutableState<T>,
    val enterTransition: EnterTransition? = fadeIn(),
    val exitTransition: ExitTransition? = fadeOut(),
) : MutableStateNarrationScope<T, StateNarrationComposable<T>>(
    uuid,
    state
) {

    private var isAnimating = false
    private var endedAnimation = false

    //TODO: Bug requires this field to be here in compose else the setter won't exist in it's sub classes
    override var currentValue
        get() = state.value
        set(value) {
            state.value = value
        }


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
            val currentKey = currentValue.key
            when (val content = composables[currentKey]) {
                null -> println("Failed to load content")
                else -> Narrate(content, currentKey, currentValue)
            }
        } else AnimatedContent(
            currentValue,
            transitionSpec = { enterTransition togetherWith (exitTransition ?: fadeOut()) }
        ) { value ->
            val currentKey = currentValue.key
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
