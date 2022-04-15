package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.debug.debug

abstract class BackStack<Composer, Collection>(protected val stack: Collection) {

    abstract val current: Composer

    abstract val isEmpty: Boolean

    abstract val isAlmostEmpty: Boolean

    protected abstract fun pop(): Boolean

    private val onEmptyListeners by lazy {
        mutableListOf<() -> Boolean>()
    }

    abstract fun add(composer: Composer)

    fun back(onEmptyStack: (() -> Boolean)? = null): Boolean = if (isAlmostEmpty) {
        exit()
    } else {
        pop()
        false
    }

    open fun exit(): Boolean {
        var closed = true

        onEmptyListeners.forEach {
            closed = closed && it()
        }

        return closed
    }

    fun addOnEmptyListener(listener: () -> Boolean) = onEmptyListeners.add(listener)

    companion object {
        private const val TAG = "BackStack"
    }
}