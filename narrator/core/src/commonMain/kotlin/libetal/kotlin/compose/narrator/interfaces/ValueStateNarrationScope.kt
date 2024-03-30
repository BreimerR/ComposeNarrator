package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.compose.narrator.PremiseKey

abstract class ValueStateNarrationScope<T, C>(
    uuid: String,
    state: T
) : StateNarrationScope<T, C, T, PremiseKey<T>>(
    uuid,
    state
)
