package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

/**@Description
 * This BackStack just adds and removes items from the
 * list of items and doesn't care about to identify
 * of the item and if any is assigned then searching for the items
 * would be required.
 * @Author brymher@gmail.com
 * */
abstract class ListBackStack<Key>(override val stack: MutableList<Key>, val onEmpty: () -> Boolean) :
    BackStack<Key, MutableList<Key>>() {

    constructor(vararg composer: Key, onEmpty: () -> Boolean) : this(mutableStateListOf(*composer), onEmpty)

    override val isEmpty: Boolean
        get() = stack.isEmpty()

    override val current
        get() = stack.lastOrNull() ?: throw EmptyBackStackException()

    override val isAlmostEmpty: Boolean
        get() = stack.size <= 1

    /**
     * This makes the required view
     * the bottom item in the LIFO
     * */
    fun navigateTo(key: Key) {
        if (isEmpty) {
            stack.add(key)
            return
        }

        if (key == current) return

        if (key in stack)
            stack.remove(key)

        stack.add(key)
    }

    public override fun pop(): Boolean {
        stack.removeAt(stack.size - 1)
        return true
    }

    fun clear() = stack.clear()

}