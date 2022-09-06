package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope


interface NarrationScope<Key : Any, ComposableFun> {

    val currentKey: Key
    val currentComponent
        get() = composables[currentKey]

    val currentNarrativeScope: NarrativeScope

    val composables: MutableMap<Key, ComposableFun>

    val narrativeScopes: MutableMap<Key, NarrativeScope>

    val children: MutableList<NarrationScope<Key, ComposableFun>>

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, ComposableFun>) -> Boolean>?>

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

        var shouldExit = true

        val listeners = onNarrativeExitRequest[currentKey]

        if (listeners != null) {
            for (listener in listeners) {
                shouldExit = shouldExit && listener(this)
                if (!shouldExit) return false
            }
        }

        for (child in children) {
            shouldExit = shouldExit && child.back()
            if (!shouldExit) return false
        }

        if (shouldExit) cleanUp(currentKey)

        return shouldExit

    }

    fun cleanUp(key:Key){
        onNarrativeExitRequest[key]?.clear()
        onNarrativeExitRequest[key] = null
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

    fun NarrativeScope.addOnExitRequest(action: () -> Boolean) = addOnExitRequest(this@NarrationScope, action)

    fun onCurrentKeyExitRequestListener(action: (NarrationScope<Key, ComposableFun>) -> Boolean) {
        currentKey.addOnNarrativeExitRequest(action)
    }

}