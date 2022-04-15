package libetal.kotlin.compose.narrator

import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.backstack.ListBackStack

class StateNarrationBackStack<T>(activities: SnapshotStateList<StateNarrationKey<T>>) :
    ListBackStack<StateNarrationKey<T>>(activities) {

    private val isFull
        get() = stack.size >= 2

    override fun add(composer: StateNarrationKey<T>) {
        stack.add(composer)
    }

    fun remove(key: StateNarrationKey<T>) = stack.remove(key)

}