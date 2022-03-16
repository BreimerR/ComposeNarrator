package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*

/**@Description
 * Controls a composables switching between different components.
 * It also controls component transitions.
 *
 * The goal is to support for
 * Tabs
 * Application Navigation
 * Overlay components if possible
 * 1. Receive platform specific events.
 *     1. BackButton. (If a new narration starts within this narration )
 * 2. Send platform specific events.
 *
 * Needs to be lifecycle aware to prevent UI thread wastage
 **/

/** TODO
 * BUG look at
 * /home/breimer/Videos/recordings/2022-03-16 03-22-52.mkv
 *
 **/
@Composable
fun <Key : Enum<*>> Narration(
    onNarrationEnd: (() -> Boolean)? = null,
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    prepareNarrations: NarrationScope<Key>.() -> Unit
) {

    val narrations = remember { mutableStateListOf<Key>() }

    val backStack = NarrationBackStack(narrations).apply {
        onNarrationEnd?.let {
            addOnEmptyListener(it)
        }
    }

    val scope = NarrationScope(
        backStack,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    prepareNarrations(scope)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        scope.narrate()
    }

}

@Composable
fun <Key : Enum<*>> Narrator(
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    content: @Composable () -> Unit
) {
    val narrations = remember { mutableStateListOf<Key>() }

    val scope = NarrationScope(
        NarrationBackStack(narrations),
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        content()
    }

}

@Composable
fun <Key> Narrate(onNarrationEnd: () -> Boolean, prepareNarrations: NarrationScope<Key>.() -> Unit) {
    val scope = LocalNarrationScope.current as NarrationScope<Key>
    scope.backStack.addOnEmptyListener(onNarrationEnd)
    prepareNarrations(scope)
    scope.narrate()
}