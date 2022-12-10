package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.interfaces.MutableStateNarrationScope
import libetal.kotlin.compose.narrator.interfaces.SnapShotStateNarrationScope


@Composable
actual fun <T> Narration(
    state: MutableState<T>,
    prepareNarrations: MutableStateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.() -> Unit
): Unit = TODO("Implementation Pending")

@Composable
actual fun <T> Narration(
    state: SnapshotStateList<T>,
    prepareNarrations: SnapShotStateNarrationScope<T, @Composable StateNarrativeScope.(SnapshotStateList<T>) -> Unit>.() -> Unit
) {
    TODO("Implementation Pending")
}