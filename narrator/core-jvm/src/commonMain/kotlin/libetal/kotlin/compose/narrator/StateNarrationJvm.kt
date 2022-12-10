package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope


@Composable
fun <T> NarrationJvm(
    state: MutableState<T>,
    enterTransition: EnterTransition? = fadeIn(),
    exitTransition: ExitTransition? = fadeOut(),
    prepareNarratives: MutableStateNarrationScopeImplJvm<T>.() -> Unit
) {

    val uuid = prepareNarratives.hashCode()

    val scope = MutableStateNarrationScopeImplJvm(
        uuid = uuid.toString(),
        state = remember { state },
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    LocalNarrationScope.current?.addChild(scope)

    for (collector in scopeCollectors) {
        collector collect scope
    }

    scopeCollectors.clear()

    prepareNarratives(scope)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        scope.Narrate()
    }

}


@Composable
fun <T> NarrationJvm(
    state: SnapshotStateList<T>,
    enterTransition: EnterTransition? = fadeIn(),
    exitTransition: ExitTransition? = fadeOut(),
    prepareNarratives: SnapshotStateNarrationScopeImplJvm<T>.() -> Unit
) {

    val uuid = prepareNarratives.hashCode()

    val scope = SnapshotStateNarrationScopeImplJvm(
        uuid = uuid.toString(),
        state = remember { state },
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    LocalNarrationScope.current?.addChild(scope)

    for (collector in scopeCollectors) {
        collector collect scope
    }

    scopeCollectors.clear()

    prepareNarratives(scope)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        scope.Narrate()
    }

}

