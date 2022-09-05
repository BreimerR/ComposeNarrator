package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.compose.narrator.backstack.ListBackStack

interface ProgressiveNarrationScope<Key, C> : NarrationScope<Key, C> {

    val backStack: ListBackStack<Key>

    override val currentKey: Key
        get() = backStack.current

    /**
     * Adds a view to the current
     * composition backStack
     **/
    override fun Key.add(content: C) {
        addToBackstack()
        composables[this] = content
    }

    fun Key.addToBackstack() {
        if (backStack.isEmpty) backStack.add(this)
    }

    // ASK ME LAST
    override fun back(): Boolean {
        if (super.back()) backStack.pop()

        return backStack.isEmpty

    }

    companion object {
        const val TAG = "ProgressiveNarrationScope"
    }

}



