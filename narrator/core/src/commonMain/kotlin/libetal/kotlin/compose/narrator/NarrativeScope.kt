package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.laziest

abstract class NarrativeScope {

    private val onExitRequestListeners: MutableList<() -> Boolean> by laziest {
        mutableListOf()
    }

    @Composable
    fun addOnExitRequest(action: () -> Boolean) {

        LocalNarrationScope.current?.onCurrentKeyExitRequestListener {
            this@NarrativeScope.back()
        }

        if (action in onExitRequestListeners) return
        onExitRequestListeners.add(action)
    }

    private fun back(): Boolean {
        var exit = true

        for (listener in onExitRequestListeners) {
            exit = exit && listener()
        }

        return exit
    }

}