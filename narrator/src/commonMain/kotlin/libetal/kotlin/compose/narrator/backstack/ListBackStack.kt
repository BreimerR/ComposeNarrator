package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.mutableStateListOf

/**@Description
 * This BackStack just adds and removes items from the
 * list of items and doesn't care about to identify
 * of the item and if any is assigned then searching for the items
 * would be required.
 * @Author brymher@gmail.com
 * */
abstract class ListBackStack<Key>(stack: MutableList<Key>) : BackStack<Key, MutableList<Key>>(stack) {

    constructor(vararg composer: Key) : this(mutableStateListOf(*composer))

    override val isEmpty: Boolean
        get() = stack.isEmpty()

    override val current
        get() = stack.lastOrNull() ?: throw EmptyBackStackException()

    var previous: Key? = null

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
        previous = current
        stack.removeAt(stack.size - 1)
        return true
    }

    override fun exit(): Boolean = super.exit().also {
        if (!isEmpty) stack.clear()
    }

}