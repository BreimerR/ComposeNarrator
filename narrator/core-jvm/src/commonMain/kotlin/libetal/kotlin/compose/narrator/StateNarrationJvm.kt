package libetal.kotlin.compose.narrator

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.StateNarrationScope


@Composable
fun <T> NarrationJvm(
    state: MutableState<T>,
    enterTransition: EnterTransition? = fadeIn(),
    exitTransition: ExitTransition? = fadeOut(),
    prepareNarratives: ListBasedStateNarrationScopeImplJvm<T>.() -> Unit
) {

    val uuid = prepareNarratives.hashCode()

    val scope = ListBasedStateNarrationScopeImplJvm(
        uuid = uuid.toString(),
        state = remember { state },
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        stack = remember { mutableStateListOf() }
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
