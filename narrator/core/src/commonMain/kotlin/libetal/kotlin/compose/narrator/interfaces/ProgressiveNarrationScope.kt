package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.compose.narrator.ProgressiveNarrativeScope
import libetal.kotlin.compose.narrator.backstack.ListBackStack

interface ProgressiveNarrationScope<Key : Any, C> : NarrationScope<Key, ProgressiveNarrativeScope, C> {

    val backStack: ListBackStack<Key>

    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty

    override val currentKey: Key
        get() = backStack.current

    /**
     * Adds a view to the current
     * composition backStack
     **/
    override fun add(key: Key, content: C) {
        key.addToBackstack()
        composables[key] = content
    }

    override val newNarrativeScope
        get() = ProgressiveNarrativeScope()

    fun Key.addToBackstack() {
        if (backStack.isEmpty) backStack.add(this)
    }

    /**
     * TODO
     * Navigates away from
     * the current narrative
     * There is need to clean up a few places
     **/
    fun Key.narrate() {
        if (currentNarrativeScope.hasCliffhangers) {
            val previous = currentKey
            val existedInStack = backStack.navigateTo(this)
            if (existedInStack) {
                backStack.invalidate(previous)
                cleanUp(previous)
            }
        }
    }

    override fun back(): Boolean {
        if (super.back()) backStack.pop()

        return backStack.isEmpty
    }

    companion object {
        const val TAG = "ProgressiveNarrationScope"
    }

}



