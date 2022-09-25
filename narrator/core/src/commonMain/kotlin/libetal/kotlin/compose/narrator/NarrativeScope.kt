package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener
import libetal.kotlin.laziest

abstract class NarrativeScope {

    val onExitRequestListeners: MutableList<() -> Boolean> by laziest {
        mutableListOf()
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