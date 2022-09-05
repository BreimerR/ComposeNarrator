package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener


interface NarrationScope<Key : Any, ComposableFun> {

    val currentKey: Key

    val currentComponent: ComposableFun?

    val currentNarrativeScope: NarrativeScope

    val composables: MutableMap<Key, ComposableFun>

    val narrativeScopes: MutableMap<Key, NarrativeScope>

    val onExitRequestListeners: MutableList<ExitRequestListener>

    val children: MutableList<NarrationScope<Key, ComposableFun>>

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ComposableFun>) -> Boolean>>

    /**
     * Adds a view to the current
     * composition backStack
     **/
    fun add(key: Key, content: ComposableFun) {
        if (key in composables) throw RuntimeException("Can't initialize same key twice")
        composables[key] = content
    }

    /**
     * backstack
     **/
    fun back(): Boolean {

        var childrenExit = true

        onNarrativeExitRequest[currentKey]?.let { listeners ->
            for (listener in listeners) {
                childrenExit = childrenExit && listener(this)
            }
        }

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
            addOnNarrativeExitRequest(it)

        }
        add(this, content)
    }

    fun Key.addOnNarrativeExitRequest(onExitRequest: (NarrationScope<Key, ComposableFun>) -> Boolean) {
        val requestListeners =
            onNarrativeExitRequest[this] ?: mutableListOf<(NarrationScope<Key, ComposableFun>) -> Boolean>().also {
                onNarrativeExitRequest[this] = it
            }

        if (onExitRequest !in requestListeners) requestListeners.add(onExitRequest)

    }

    fun NarrativeScope.addOnExitRequest(action: () -> Boolean)  = addOnExitRequest(this@NarrationScope, action)

    fun onCurrentKeyExitRequestListener(action: (NarrationScope<Key, ComposableFun>) -> Boolean) {
        currentKey.addOnNarrativeExitRequest(action)
    }

}