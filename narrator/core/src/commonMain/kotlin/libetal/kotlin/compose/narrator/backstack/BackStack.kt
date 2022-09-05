package libetal.kotlin.compose.narrator.backstack

import libetal.kotlin.laziest

abstract class BackStack<Key, Collection>(protected val stack: Collection) {

    abstract val current: Key

    abstract val isEmpty: Boolean

    abstract val isAlmostEmpty: Boolean

    protected abstract fun pop(): Key

    private val onEmptyListeners by laziest {
        mutableListOf<() -> Boolean>()
    }

    abstract fun add(composer: Key)

    fun back(onEmptyStack: (() -> Boolean)? = null): Boolean = if (isAlmostEmpty) {
        onEmptyStack?.invoke()
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