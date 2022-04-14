package libetal.kotlin.compose.narrator

import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.ListBackStack

class NarrationBackStack<Key>(activities: SnapshotStateList<Key>) : ListBackStack<Key>(activities) {
    override fun add(composer: Key) {
        if (stack.isEmpty()) navigateTo(composer)
    }
}

