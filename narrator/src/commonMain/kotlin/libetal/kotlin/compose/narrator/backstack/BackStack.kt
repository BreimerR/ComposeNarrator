package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class BackStack<Index>(indexes: SnapshotStateList<Index> = mutableStateListOf()) {

    val hasStack: Boolean
        get() = !hasNoStack

    val hasNoStack
        get() = indexes.isEmpty()

    val active: Index
        get() = indexes.lastOrNull() ?: throw EmptyBackStackException()

    val indexes by lazy {
        indexes
    }

    suspend fun previous(onEmptyStack: () -> Unit = {}) = if (indexes.isEmpty()) onEmptyStack()
    else indexes.removeRange(indexes.size - 1, indexes.size)


    suspend fun navigateTo(index: Index): Index {
        if (index in indexes) return index

        indexes.add(index)

        return index
    }

    suspend fun clear() = indexes.removeRange(0, indexes.size)

}

