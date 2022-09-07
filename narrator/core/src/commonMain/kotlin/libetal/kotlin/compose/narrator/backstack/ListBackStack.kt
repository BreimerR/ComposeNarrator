package libetal.kotlin.compose.narrator.backstack

import androidx.compose.runtime.mutableStateListOf
import libetal.kotlin.compose.narrator.backstack.exceptions.EmptyBackStackException
import libetal.kotlin.debug.info

/**@Description
 * This BackStack just adds and removes items from the
 * list of items and doesn't care about the identity
 * of the item and if any is assigned then searching for the items
 * would be required.
 * @Author brymher@gmail.com
 * */
abstract class ListBackStack<Key>(stack: MutableList<Key>) : BackStack<Key, MutableList<Key>>(stack) {

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
    fun navigateTo(key: Key): Boolean {
        var existedInStack = false
        if (isEmpty) {
            stack.add(key)
            return existedInStack
        }

        if (key == current) return existedInStack

        if (key in stack) {
            existedInStack = true
            stack.remove(key)
        }

        stack.add(key)

        return existedInStack
    }

    public override fun pop(): Key = current.also {
        previous = it
        stack.removeAt(stack.size - 1)
    }

    fun invalidate(key: Key) = stack.remove(key)

    override fun add(composer: Key) {
        stack.add(composer)
    }

    override fun exit(): Boolean = super.exit().also {
        stack.clear()
    }

    companion object {
        const val TAG = "ListBackStack"
    }

}