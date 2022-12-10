package libetal.kotlin.compose.narrator.interfaces


import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.PremiseKey

interface SnapShotStateNarrationScope<T, C> :
    StateNarrationScope<T, C, SnapshotStateList<T>, PremiseKey<SnapshotStateList<T>>>