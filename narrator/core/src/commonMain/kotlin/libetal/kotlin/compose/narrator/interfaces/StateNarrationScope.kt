package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.StateNarrationKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C> : NarrationScope<String, StateNarrativeScope, C> {

    val state: MutableState<T>

    var currentValue
        get() = state.value
        set(value) {
            state.value = value
        }

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors: MutableMap<String, StateNarrationKey<T>>

    fun createPremise(premise: (T) -> Boolean) = premise.hashCode().toString().also {
        stateSelectors[it] = premise
    }

}