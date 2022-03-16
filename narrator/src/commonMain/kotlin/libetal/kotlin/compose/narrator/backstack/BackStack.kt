package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList

abstract class BackStack<Composer, Collection> {

    abstract val stack: Collection

    abstract val current: Composer

    abstract val isEmpty: Boolean

    abstract val isAlmostEmpty: Boolean

    protected abstract fun pop(): Boolean

    private val onEmptyListeners: MutableList<() -> Boolean> = mutableListOf()

    fun back(onEmptyStack: (() -> Boolean)? = null): Boolean = if (isAlmostEmpty) {

        var closed = true

        onEmptyStack?.let {
            addOnEmptyListener(it)
        }

        onEmptyListeners.forEach {
            closed = closed && it()
        }

        closed

    } else pop()

    fun addOnEmptyListener(listener: () -> Boolean) = onEmptyListeners.add(listener)

    companion object {
        operator fun <Activity> invoke(vararg activity: Activity) =
            object : ListBackStack<Activity>(*activity) {} as BackStack<Activity, SnapshotStateList<Activity>>
    }

}