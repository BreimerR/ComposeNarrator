package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

abstract class NarrativeScope {

    private val onExitRequestListeners: MutableList<() -> Boolean> by laziest {
        mutableListOf()
    }

    internal fun <Key : Any ,Scope:NarrativeScope, Content> addOnExitRequest(
        narrationScope: NarrationScope<Key,Scope, Content>,
        action: ExitRequestListener
    ) {
        narrationScope.onCurrentKeyExitRequestListener {
            hasCliffhangers
        }

        if (action in onExitRequestListeners) return
        onExitRequestListeners.add(action)
    }

    val hasCliffhangers
        get(): Boolean {
            var exit = true

            for (listener in onExitRequestListeners) {
                exit = exit && listener()
            }

            return exit
        }

}