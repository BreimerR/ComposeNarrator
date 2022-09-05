package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList

class NarrationBackStack<Key>(stack: SnapshotStateList<Key>) : ListBackStack<Key>(stack)