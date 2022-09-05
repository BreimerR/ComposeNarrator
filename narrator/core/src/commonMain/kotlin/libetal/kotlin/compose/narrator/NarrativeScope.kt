package libetal.kotlin.compose.narrator

import libetal.kotlin.laziest

class NarrativeScope {

    val hasExitListeners
        get() = onExitRequestListeners.isNotEmpty()

    private val onExitRequestListeners: MutableList<() -> Boolean> by laziest {
        mutableListOf()
    }

    fun addOnExitRequest(action: () -> Boolean) {
        if (action in onExitRequestListeners) return
        onExitRequestListeners.add(action)
    }

    fun back(): Boolean {
        var exit = true

        for (listener in onExitRequestListeners) {
            exit = exit && listener()
        }

        return exit
    }

}