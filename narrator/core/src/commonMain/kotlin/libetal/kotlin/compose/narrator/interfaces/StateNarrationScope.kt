package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.StateNarrativeScope

interface StateNarrationScope<T, C> : NarrationScope<Int, C> {

    val state: MutableState<T>

    override val newNarrativeScope
        get() = StateNarrativeScope()

    val stateSelectors: MutableMap<Int, (T) -> Boolean>

    operator fun ((T) -> Boolean).invoke(onExitRequest: ((NarrationScope<Int, C>) -> Boolean)? = null, content: C) {
        val hashCode = hashCode()
        stateSelectors[hashCode] = this
        hashCode.invoke(onExitRequest, content)
    }

}