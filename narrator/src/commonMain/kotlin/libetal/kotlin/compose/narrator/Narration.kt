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
    onNarrationEnd: () -> Boolean,
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    prepareNarrations:  NarrationScope<Key>.() -> Unit
) {

    val activities = remember { mutableStateListOf<Key>() }
    val backStack = NarrationBackStack(activities, onNarrationEnd)

    /**
     * Moving this outside causes narration
     * to loop back to the previous narration
     **/
    val narrationScope = NarrationScope(
        backStack,
        enterTransition,
        exitTransition
    )

    CompositionLocalProvider(LocalNarrationScope provides narrationScope) {
        /**
         * Preparing this outside the scope of this causes' app
         * to fail not sure about providers and how they manage data yet
         * */
        narrationScope.prepareNarrations()

        narrationScope.narrate()
    }

    DisposableEffect(backStack) {

        onDispose {
            backStack.clear()
        }

    }
}

