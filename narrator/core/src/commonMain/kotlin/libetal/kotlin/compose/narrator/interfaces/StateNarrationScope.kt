package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.NarrationStateKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C> : NarrationScope<Int, C> {

    val state: MutableState<T>

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors:MutableMap<Int,NarrationStateKey<T>>

    operator fun NarrationStateKey<T>.invoke(onExitRequest: ((NarrationScope<Int, C>) -> Boolean)? = null, content: C) =
        hashCode().invoke(onExitRequest, content)

}