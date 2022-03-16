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
    prepareNarrations: NarrationScope<Key>.() -> Unit
) {

    RunInNarrationScope<Key>(onNarrationEnd, enterTransition, exitTransition) {
        Narrate(prepareNarrations)
    }

}

@Composable
fun <Key : Enum<*>> NarrationScope<Key>.Narrate(
    prepareNarrations: NarrationScope<Key>.() -> Unit
) {

    prepareNarrations()

    Compose {
        narrate()
    }

}


@Composable
fun <Key> NarrationScope<Key>.Compose(content: @Composable NarrationScope<Key>.() -> Unit) {
    CompositionLocalProvider(LocalNarrationScope provides this) {
        content()
    }
}

@Composable
fun <Key> Narration(
    onNarrationEnd: () -> Boolean = { false },
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    content: @Composable NarrationScope<Key>.() -> Unit
) {
    RunInNarrationScope<Key>(onNarrationEnd, enterTransition, exitTransition) {
        Compose(content)
    }

}

@Composable
internal fun <Key> RunInNarrationScope(
    onNarrationEnd: () -> Boolean = { false },
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    compose: @Composable NarrationScope<Key>.() -> Unit
) {
    /**
     * Moving this outside causes narration
     * to loop back to the previous narration
     **/
    NarrationScope(
        NarrationBackStack(
            remember { mutableStateListOf<Key>() },
            onNarrationEnd
        ),
        enterTransition,
        exitTransition
    ).compose()

}