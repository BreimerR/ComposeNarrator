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

val defaultEntryAnimation
    get() = slideInVertically { height -> height } + fadeIn()

val defaultExitAnimation
    get() = slideOutVertically { height -> height / 2 } + fadeOut()

/** TODO
 * BUG look at
 * /home/breimer/Videos/recordings/2022-03-16 03-22-52.mkv
 *
 **/
@Composable
fun <Key> Narration(
    onNarrationEnd: (() -> Boolean)? = null,
    enterTransition: EnterTransition = defaultEntryAnimation,
    exitTransition: ExitTransition = defaultExitAnimation,
    prepareNarrations: NarrationScope<Key>.() -> Unit
) {

    val narrations = remember { mutableStateListOf<Key>() }

    val backStack = NarrationBackStack(narrations).apply {
        /**
         * Avoid using till your able to
         * fix looping in added scopes
         **/
       /* onNarrationEnd?.let {
            addOnEmptyListener(it)
        }*/
    }

    val scope = NarrationScope(
        backStack,
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    LocalNarrationScope.current?.add("${prepareNarrations.hashCode()}", scope)

    prepareNarrations(scope)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        scope.narrate()
    }

}


@Composable
fun <T> Narration(
    state: MutableState<T>,
    exitState: T,
    onNarrationEnd: (() -> Boolean)? = null,
    prepareNarrations: StateNarrationScope<T>.() -> Unit
) {
    val controlState = remember { state }
    val narrations = remember { mutableStateListOf<StateNarrationKey<T>>() }

    val backStack = StateNarrationBackStack(narrations).apply {
        /**
         * Avoid using till your able to
         * fix looping in added scopes
         **/
        /* onNarrationEnd?.let {
             addOnEmptyListener(it)
         }*/
    }

    val scope = StateNarrationScope(backStack, controlState, exitState)

    LocalNarrationScope.current?.add("${prepareNarrations.hashCode()}", scope)

    prepareNarrations(scope)

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        scope.narrate()
    }

}



