package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun <Key> Narrator(
    enterTransition: EnterTransition = defaultEntryAnimation,
    exitTransition: ExitTransition = defaultExitAnimation,
    content: @Composable NarratorScope<Key>.() -> Unit
) {
    val narrations = remember { mutableStateListOf<Key>() }

    val scope = NarrationScope(
        NarrationBackStack(narrations),
        enterTransition = enterTransition,
        exitTransition = exitTransition
    )

    CompositionLocalProvider(LocalNarrationScope provides scope) {
        content(NarratorScope())
    }

}
