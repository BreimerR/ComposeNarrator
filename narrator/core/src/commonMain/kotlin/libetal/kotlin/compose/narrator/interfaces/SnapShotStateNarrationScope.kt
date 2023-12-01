package libetal.kotlin.compose.narrator.interfaces


import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.PremiseKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

abstract class SnapShotStateNarrationScope<T, C>(
    uuid: String,
    state: SnapshotStateList<T>,
) : StateNarrationScope<T, C, SnapshotStateList<T>, PremiseKey<SnapshotStateList<T>>>(
    uuid,
    state
)
