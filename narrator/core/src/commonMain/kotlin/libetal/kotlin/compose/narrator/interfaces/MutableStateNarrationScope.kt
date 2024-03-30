package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.PremiseKey

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
