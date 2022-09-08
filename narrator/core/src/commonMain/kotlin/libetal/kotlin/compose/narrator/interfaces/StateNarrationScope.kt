package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.StateNarrationKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C> : NarrationScope<Int, (T) -> Boolean, C> {

    val state: MutableState<T>

    override val ((T) -> Boolean).key
        get() = (hashCode()).also {
            if (it !in stateSelectors)
                stateSelectors[it] = this
        }

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors: MutableMap<Int, StateNarrationKey<T>>

}