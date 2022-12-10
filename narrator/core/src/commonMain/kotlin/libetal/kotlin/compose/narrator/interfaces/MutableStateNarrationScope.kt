package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.PremiseKey

interface MutableStateNarrationScope<T, C> : StateNarrationScope<T, C, MutableState<T>, PremiseKey<T>> {

    var currentValue
        get() = state.value
        set(value) {
            state.value = value
        }

}


