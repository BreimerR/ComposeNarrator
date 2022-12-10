package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.SnapShotStateNarrationScope
import libetal.kotlin.laziest

class SnapshotStateNarrationScopeImplJvm<T>(
    override val uuid: String,
    override val state: SnapshotStateList<T>,
    val enterTransition: EnterTransition? = fadeIn(),
    val exitTransition: ExitTransition? = fadeOut()
) : SnapShotStateNarrationScope<T, StateNarrationComposable<SnapshotStateList<T>>> {

    override val stateSelectors by laziest {
        mutableMapOf<String, (SnapshotStateList<T>) -> Boolean>()
    }

    override val composables by laziest {
        mutableMapOf<String, StateNarrationComposable<SnapshotStateList<T>>>()
    }

    override val narrativeScopes by laziest {
        mutableMapOf<String, StateNarrativeScope>()
    }

    override val onNarrationEndListeners by laziest {
        mutableMapOf<String, MutableList<() -> Unit>>()
    }

    override val children by laziest {
        mutableMapOf<String, NarrationScope<out Any, out NarrativeScope, StateNarrationComposable<SnapshotStateList<T>>>>()
    }

    override val onNarrativeExitRequest by laziest {
        mutableMapOf<String, MutableList<(NarrationScope<String, StateNarrativeScope, StateNarrationComposable<SnapshotStateList<T>>>) -> Boolean>?>()
    }

    private val String.currentNarrativeScope
        get() = narrativeScopes[this] ?: newNarrativeScope.also {
            narrativeScopes[this] = it
        }

    private val SnapshotStateList<T>.key: String
        get() {

            for ((hashCode, selector) in stateSelectors) {
                if (selector(this)) return hashCode

            }

            throw RuntimeException("Failed to resolve a narration. Check your created premises")

        }

    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun Narrate() {
        if (enterTransition == null) {
            val currentKey = state.key
            composables[currentKey]?.Narrate(currentKey, state)
        } else AnimatedContent(
            state,
            transitionSpec = {
                enterTransition with (exitTransition ?: fadeOut())
            }
        ) { snapshot ->
            val currentKey = snapshot.key
            composables[currentKey]?.Narrate(currentKey, snapshot)
        }
    }

    @Composable
    fun StateNarrationComposable<SnapshotStateList<T>>.Narrate(key: String, state: SnapshotStateList<T>) {
        this(key.currentNarrativeScope, state)
    }

}