package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener


interface NarrationScope<Key, ComposableFun> {

    val currentKey: Key

    val currentComponent: ComposableFun?

    val composables: MutableMap<Key, ComposableFun>

    val onExitRequestListeners: MutableList<ExitRequestListener>

    val children: MutableList<NarrationScope<Key, ComposableFun>>

    val onNarrativeExitRequest: MutableMap<Key, (NarrationScope<Key, ComposableFun>) -> Boolean>

    /**
     * Adds a view to the current
     * composition backStack
     **/
    fun Key.add(content: ComposableFun) {
        if (this in composables) throw RuntimeException("Can't initialize same key twice")
        composables[this] = content
    }

    /**
     * backstack
     **/
    fun back(): Boolean {

        var childrenExit = onNarrativeExitRequest[currentKey]?.invoke(this) ?: true

        for (child in children) {
            childrenExit = childrenExit && child.back()
            if (!childrenExit) return false
        }

        return childrenExit

    }

    @Composable
    fun Narrate() {

        when (val composable: (ComposableFun)? = currentComponent) {
            null -> throw RuntimeException("Failed to retrieve component")
            else -> Narrate(composable)
        }
    }

    @Composable
    fun Narrate(composable: ComposableFun)

    operator fun Key.invoke(onExitRequest: ((NarrationScope<Key, ComposableFun>) -> Boolean)? = null, content: ComposableFun) {
        onExitRequest?.let {
            onNarrativeExitRequest[this] = it
        }
        add(content)
    }

    /**
     * Makes it possible to monitor back pressed per narration and prevent
     * an exit if possible
     **/
    fun onExitRequest(action: ExitRequestListener) {
        if (action in onExitRequestListeners) return

        onExitRequestListeners.add(action)
    }

}