package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.StateNarrativeScope

abstract class StateNarrationScope<T, C, S, Premise>(
    uuid: String,
    val state: S
) : NarrationScope<String, StateNarrativeScope, C>(
    uuid,
    StateNarrativeScope()
) {

    val stateSelectors: MutableMap<String, Premise> = mutableMapOf()

    fun createPremise(premise: Premise) = premise.hashCode().toString().also {
        stateSelectors[it] = premise
    }

}
