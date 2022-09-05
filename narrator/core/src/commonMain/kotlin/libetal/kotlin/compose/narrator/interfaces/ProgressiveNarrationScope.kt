package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.ProgressiveNarrativeScope
import libetal.kotlin.compose.narrator.backstack.ListBackStack

interface ProgressiveNarrationScope<Key : Any, C> : NarrationScope<Key, C> {

    val backStack: ListBackStack<Key>

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

    fun Key.addToBackstack() {
        if (backStack.isEmpty) backStack.add(this)
    }

    fun Key.narrate() {
        backStack.navigateTo(this)
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



