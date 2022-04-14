package libetal.kotlin.compose.narrator

import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.ListBackStack

class AdaptableNarrationBackStack(activities: SnapshotStateList<StateNarrationKey>) :
    ListBackStack<StateNarrationKey>(activities) {

    private val isFull
        get() = stack.size >= 2

    override fun add(composer: () -> Boolean) {
        stack.add(composer)
    }

    fun forEachIndexed(looper: (Int, StateNarrationKey) -> Unit) = stack.forEachIndexed(looper)

    fun remove(key: StateNarrationKey) = stack.remove(key)

}