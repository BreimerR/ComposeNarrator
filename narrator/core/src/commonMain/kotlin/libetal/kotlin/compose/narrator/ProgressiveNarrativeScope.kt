package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener

class ProgressiveNarrativeScope : NarrativeScope() {

    internal fun <Key : Any, Content> addOnExitRequest(
        narrationScope: ProgressiveNarrationScope<Key, Content>,
        action: ExitRequestListener
    ) {

        narrationScope.onCurrentKeyExitRequestListener {
            hasCliffhangers
        }

        if (action !in onExitRequestListeners)
            onExitRequestListeners.add(action)

    }

}



