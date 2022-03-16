package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList

abstract class BackStack<Composer, Collection> {

    abstract val stack: Collection

    abstract val current: Composer

    abstract val isEmpty: Boolean

    abstract val isAlmostEmpty: Boolean

    protected abstract fun pop(): Boolean

    fun back(onEmptyStack: () -> Boolean): Boolean = if (isAlmostEmpty) onEmptyStack()
    else pop()

    companion object {
        operator fun <Activity> invoke(vararg activity: Activity, onEmpty: () -> Boolean) =
            object : ListBackStack<Activity>(*activity, onEmpty = onEmpty) {} as BackStack<Activity, SnapshotStateList<Activity>>
    }

}