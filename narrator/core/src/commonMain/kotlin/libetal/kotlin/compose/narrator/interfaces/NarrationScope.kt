package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.log.debug
import libetal.kotlin.log.warn
import libetal.kotlin.log.info

interface NarrationScope<Key : Any, Scope : NarrativeScope, Content> {

    val uuid: String

    val newNarrativeScope: Scope

    val composables: MutableMap<Key, Content>

    val narrativeScopes: MutableMap<Key, Scope>


    val onNarrationEndListeners: MutableMap<Key, MutableList<() -> Unit>>

    val children: MutableMap<String, NarrationScope<out Any, out NarrativeScope, Content>>

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Scope, Content>) -> Boolean>?>

    fun add(key: Key, content: Content) {
        if (key in composables) {
            TAG warn "Can't initialize same key twice: ${this::class} $key"
            TAG info "Need Help Resolving for this"
            return
        }
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

        var shouldExit = true

        for ((_, child) in children) {
            shouldExit = shouldExit && child.back()
            if (!shouldExit) return false
        }

        return shouldExit

    }


    fun cleanUp(key: Key) {
        onNarrativeExitRequest[key]?.clear()
        onNarrativeExitRequest[key] = null
    }

    @Composable
    fun Narrate()


    @Composable
    fun Compose(composable: Content){

    }

    operator fun Key.invoke(content: Content) = add(this, content)

    fun Key.addOnNarrativeExitRequest(onExitRequest: (NarrationScope<Key, Scope, Content>) -> Boolean) {
        val requestListeners =
            onNarrativeExitRequest[this] ?: mutableListOf<(NarrationScope<Key, Scope, Content>) -> Boolean>().also {
                onNarrativeExitRequest[this] = it
            }

        if (onExitRequest !in requestListeners) requestListeners.add(onExitRequest)

    }

    fun Key.addOnNarrationEnd(action: () -> Unit) {
        val listeners = onNarrationEndListeners[this] ?: mutableListOf<() -> Unit>().also {
            onNarrationEndListeners[this] = it
        }

        if (action !in listeners)
            listeners.add(action)
    }



    companion object {
        const val TAG = "NarrationScope"
    }

}