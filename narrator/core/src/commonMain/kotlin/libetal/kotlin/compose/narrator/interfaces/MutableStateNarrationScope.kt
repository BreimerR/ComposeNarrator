package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.PremiseKey
import libetal.kotlin.compose.narrator.StateNarrativeScope

abstract class MutableStateNarrationScope<T, C>(
    uuid: String,
    state: MutableState<T>
) : StateNarrationScope<T, C, MutableState<T>, PremiseKey<T>>(
    uuid,
    state
) {

    open var currentValue
        get() = state.value
        set(value) {
            state.value = value
        }

}
