package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList

class BackStack<Index>(private val indexes: SnapshotStateList<Index>) {

    /**TODO
     * val hasStack: Boolean
     *     get() = !hasNoStack
     * val hasNoStack
     *     get() = indexes.isEmpty()
     **/
    val active: Index
        get() = indexes.lastOrNull() ?: throw EmptyBackStackException()

    fun previous(onEmptyStack: () -> Unit, onSuccessfulNarrationSwitch: () -> Unit = {}) = if (indexes.size <= 1) onEmptyStack()
    else {
        indexes.removeRange(indexes.size - 1, indexes.size)
    }

    fun navigateTo(index: Index): Index {
        if (index in indexes) return index

        indexes.add(index)

        return index
    }

}

