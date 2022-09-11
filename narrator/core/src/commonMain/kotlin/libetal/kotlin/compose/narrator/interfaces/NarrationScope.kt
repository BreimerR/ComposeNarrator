package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.debug.info


interface NarrationScope<Key : Any, Scope : NarrativeScope, Content> {

    val uuid: String

    val currentKey: Key

    val shouldExit: Boolean

    val currentComponent
        get() = composables[currentKey]

    val newNarrativeScope: Scope

    val composables: MutableMap<Key, Content>

    val narrativeScopes: MutableMap<Key, Scope>

    val currentNarrativeScope: Scope
        get() = narrativeScopes[currentKey] ?: newNarrativeScope.also {
            narrativeScopes[currentKey] = it
        }

    val onNarrationEndListeners: MutableList<() -> Unit>

    val children: MutableMap<String, NarrationScope<out Any, out NarrativeScope, Content>>

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Scope, Content>) -> Boolean>?>


    /**
     * Adds a view to the current
     * composition backStack
     **/
    fun add(key: Key, content: Content) {
        if (key in composables) throw RuntimeException("Can't initialize same key twice")
        composables[key] = content
    }

    fun addChild(child: NarrationScope<*, *, *>) {
        @Suppress("UNCHECKED_CAST")
        (child as NarrationScope<out Any, out NarrativeScope, Content>)

        if (child.uuid !in children) children[child.uuid] = child
    }


    /**
     * backstack
     **/
    fun back(): Boolean {
        if (shouldExit) {
            cleanUp(currentKey)
            for (listener in onNarrationEndListeners) {
                listener()
            }
            return true
        }

        var shouldExit = runNarrativeExitRequestListeners(true)

        if (!shouldExit) return false

        for ((uuid, child) in children) {
            shouldExit = shouldExit && child.back()
            TAG info "${child::class} exiting = $shouldExit"
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
    fun Narrate() = when (val composable: Content? = currentComponent) {
        null -> throw RuntimeException("Failed to retrieve component")
        else -> Narrate(composable)
    }

    @Composable
    fun Narrate(composable: Content)

    @Composable
    fun Compose(composable: Content)

    operator fun Key.invoke(content: Content) = add(this, content)

    fun Key.addOnNarrativeExitRequest(onExitRequest: (NarrationScope<Key, Scope, Content>) -> Boolean) {
        val requestListeners =
            onNarrativeExitRequest[this] ?: mutableListOf<(NarrationScope<Key, Scope, Content>) -> Boolean>().also {
                onNarrativeExitRequest[this] = it
            }

        if (onExitRequest !in requestListeners) requestListeners.add(onExitRequest)

    }

    fun onCurrentKeyExitRequestListener(action: (NarrationScope<Key, Scope, Content>) -> Boolean) {
        currentKey.addOnNarrativeExitRequest(action)
    }

    fun addOnNarrationEnd(action: () -> Unit) {
        if (action !in onNarrationEndListeners)
            onNarrationEndListeners.add(action)
    }

    fun Scope.addOnExitRequest(action: ExitRequestListener) = addOnExitRequest(this@NarrationScope, action)

    companion object {
        const val TAG = "NarrationScope"
    }

}