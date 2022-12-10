package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.MutableStateNarrationScope
import libetal.kotlin.laziest


class MutableStateNarrationScopeImplJvm<T>(
    override val uuid: String,
    override val state: MutableState<T>,
    val enterTransition: EnterTransition? = fadeIn(),
    val exitTransition: ExitTransition? = fadeOut()
) : MutableStateNarrationScope<T, StateNarrationComposable<T>> {

    private var isAnimating = false
    private var endedAnimation = false

    private val String.currentNarrativeScope
        get() = narrativeScopes[this] ?: newNarrativeScope.also {
            narrativeScopes[this] = it
        }

    override val stateSelectors by laziest {
        mutableMapOf<String, (T) -> Boolean>()
    }

    private val T.key: String
        get() {
            for ((hashCode, selector) in stateSelectors) {
                if (selector(this)) return hashCode

            }

            throw RuntimeException("Failed to resolve a narration. Check your created premises")

        }

    override val newNarrativeScope: StateNarrativeScope
        get() = StateNarrativeScope()

    override val composables by laziest {
        mutableMapOf<String, StateNarrationComposable<T>>()
    }

    override val narrativeScopes by laziest {
        mutableMapOf<String, StateNarrativeScope>()
    }

    override val onNarrationEndListeners by laziest {
        mutableMapOf<String, MutableList<() -> Unit>>()
    }

    override val children by laziest {
        mutableMapOf<String, NarrationScope<out Any, out NarrativeScope, StateNarrationComposable<T>>>()
    }

    override val onNarrativeExitRequest by laziest {
        mutableMapOf<String, MutableList<(NarrationScope<String, StateNarrativeScope, StateNarrationComposable<T>>) -> Boolean>?>()
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
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate() {
        if (enterTransition == null) {
            val currentKey = currentValue.key
            composables[currentKey]?.Narrate(currentKey, currentValue)
        } else AnimatedContent(
            currentValue,
            transitionSpec = { enterTransition with (exitTransition ?: fadeOut()) }
        ) { value ->
            val currentKey = value.key
            composables[currentKey]?.Narrate(currentKey, value)
        }

    }

    @Composable
    fun StateNarrationComposable<T>.Narrate(key: String, value: T) {
        this(key.currentNarrativeScope, value)
    }


    companion object {
        const val TAG = "ListBasedStateNarrationScopeImplJvm"
    }

}