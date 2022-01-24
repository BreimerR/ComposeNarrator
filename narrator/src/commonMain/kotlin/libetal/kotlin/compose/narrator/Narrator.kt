package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.view_models.ViewModel

@Composable
fun <Index : Any> Narrator(
    firstPage: Index,
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    onNarrationEnd: () -> Unit = { throw RuntimeException("Unhandled narration end. No open chapters existing") },
    chaptersInitializer: Narration<Index>.() -> Unit
) {
    val narration = Narration(firstPage, enterTransition, exitTransition, onNarrationEnd = onNarrationEnd)

    chaptersInitializer(narration)

    narration.begin()
}


@Composable
fun <VM : ViewModel, Index : Any> VM.Narrator(
    firstPage: Index,
    enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    chaptersInitializer: Narration<Index>.() -> Unit
) {
    val narration = Narration(firstPage, enterTransition, exitTransition)

    chaptersInitializer(narration)

    narration.begin()
}

@Composable
fun <Index, N : Narration<Index>> N.Narrate(
    chaptersInitializer: N.() -> Unit
) {
    chaptersInitializer()

    begin()
}
