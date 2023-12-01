package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.laziest
import libetal.kotlin.log.warn
import libetal.kotlin.log.info

abstract class NarrationScope<Key : Any, Scope : NarrativeScope, Content>(
    val uuid: String,
    val newNarrativeScope: Scope
) {

    val narrativeScopes by laziest {
        mutableMapOf<Key, Scope>()
    }

    val composables: MutableMap<Key, Content> by laziest {
        mutableMapOf()
    }

    val children: MutableMap<String, NarrationScope<out Any, out NarrativeScope, Content>> by laziest {
        mutableMapOf()
    }

    val onNarrationEndListeners by laziest {
        mutableMapOf<Key, MutableList<() -> Unit>>()
    }

    val onNarrativeExitRequest: MutableMap<Key, MutableList<(NarrationScope<Key, Scope, Content>) -> Boolean>?> by laziest {
        mutableMapOf()
    }

    @Composable
    abstract fun Narrate()

    open fun add(key: Key, content: Content) {
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
    open fun back(): Boolean {

        var shouldExit = true

        for ((_, child) in children) {
            shouldExit = shouldExit && child.back()
            if (!shouldExit) return false
        }

        return shouldExit

    }


    open fun cleanUp(key: Key) {
        onNarrativeExitRequest[key]?.clear()
        onNarrativeExitRequest[key] = null
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
