package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.backstack.ListBackStack

interface ProgressiveNarrationScope<Key : Any, C> : NarrationScope<Key, C> {

    val backStack: ListBackStack<Key>

    override val currentKey: Key
        get() = backStack.current

    override val currentNarrativeScope
        get() = narrativeScopes[currentKey] ?: createNarrative().also {
            narrativeScopes[currentKey] = it
        }


    fun createNarrative(): NarrativeScope

    /**
     * Adds a view to the current
     * composition backStack
     **/
    override fun add(key: Key, content: C) {
        key.addToBackstack()
        composables[key] = content
    }

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
            if (existedInStack)
                previous.cleanUp(existedInStack)
        }
    }

    fun Key.cleanUp(existedInStack: Boolean) {
        backStack.invalidate(this)
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



