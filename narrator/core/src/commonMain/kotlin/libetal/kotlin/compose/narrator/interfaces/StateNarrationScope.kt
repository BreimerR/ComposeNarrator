package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C, S, Premise> : NarrationScope<String, StateNarrativeScope, C> {

    val state: S

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors: MutableMap<String, Premise>

    fun createPremise(premise: Premise) = premise.hashCode().toString().also {
        stateSelectors[it] = premise
    }

}