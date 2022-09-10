package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.StateNarrationKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C> : NarrationScope<Int, StateNarrativeScope, C> {

    val state: MutableState<T>

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors: MutableMap<Int, StateNarrationKey<T>>

    fun createPremise(premise: (T) -> Boolean) = premise.hashCode().also {
        stateSelectors[it] = premise
    }

}