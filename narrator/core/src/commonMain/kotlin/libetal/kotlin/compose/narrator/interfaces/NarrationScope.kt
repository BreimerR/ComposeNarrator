package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.debug.info


interface NarrationScope<Key : Any, Invoked, ComposableFun> {

    val currentKey: Key

    val shouldExit: Boolean

    val currentComponent
        get() = composables[currentKey]

    val currentNarrativeScope: NarrativeScope
        get() = narrativeScopes[currentKey] ?: newNarrativeScope.also {
            narrativeScopes[currentKey] = it
        }

    val Invoked.key: Key

    val composables: MutableMap<Key, ComposableFun>

    val narrativeScopes: MutableMap<Key, NarrativeScope>

    val onNarrationEndListeners: MutableList<() -> Unit>

    val children: MutableList<NarrationScope<Key, Invoked, ComposableFun>>

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Invoked, ComposableFun>) -> Boolean>?>


    /**
     * Adds a view to the current
     * composition backStack
     **/
    fun add(key: Key, content: ComposableFun) {
        if (key in composables) throw RuntimeException("Can't initialize same key twice")
        composables[key] = content
    }


    val newNarrativeScope: NarrativeScope

    /**
     * backstack
     **/
    fun back(onNarrationEnd: (() -> Unit)? = null): Boolean {
        if (shouldExit) {
            TAG info "Exiting Popping item"
            cleanUp(currentKey)
            onNarrationEnd?.invoke()
            return true
        }

        var shouldExit = runNarrativeExitRequestListeners(true)

        if (!shouldExit) return false

        for (child in children) {
            shouldExit = shouldExit && child.back()
            if (!shouldExit) return false
        }

        if (shouldExit) cleanUp(currentKey)

        return shouldExit

    }

    private fun runNarrativeExitRequestListeners(shouldExit: Boolean): Boolean {
        var exit = shouldExit

        val listeners = onNarrativeExitRequest[currentKey]

        if (listeners != null) {
            for (listener in listeners) {
                exit = exit && listener(this)
                if (!exit) return false
            }
        }

        return exit
    }

    fun cleanUp(key: Key) {
        onNarrativeExitRequest[key]?.clear()
        onNarrativeExitRequest[key] = null
    }

    @Composable
    fun Narrate() = when (val composable: ComposableFun? = currentComponent) {
        null -> throw RuntimeException("Failed to retrieve component")
        else -> Narrate(composable)
    }

    @Composable
    fun Narrate(composable: ComposableFun)

    operator fun Invoked.invoke(content: ComposableFun) = add(key, content)

    fun Key.addOnNarrativeExitRequest(onExitRequest: (NarrationScope<Key, Invoked, ComposableFun>) -> Boolean) {
        val requestListeners =
            onNarrativeExitRequest[this] ?: mutableListOf<(NarrationScope<Key, Invoked, ComposableFun>) -> Boolean>().also {
                onNarrativeExitRequest[this] = it
            }

        if (onExitRequest !in requestListeners) requestListeners.add(onExitRequest)

    }

    fun onCurrentKeyExitRequestListener(action: (NarrationScope<Key, Invoked, ComposableFun>) -> Boolean) {
        currentKey.addOnNarrativeExitRequest(action)
    }

    fun addOnNarrationEnd(action: () -> Unit) {
        if (action in onNarrationEndListeners) return
        onNarrationEndListeners.add(action)
    }

    companion object {
        const val TAG = "NarrationScope"
    }

}