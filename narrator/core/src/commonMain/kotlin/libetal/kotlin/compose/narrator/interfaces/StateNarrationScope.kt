package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.NarrationStateKey

interface StateNarrationScope<Key> : NarrationScope<NarrationStateKey<Key>, @Composable () -> Unit> {
    val state: MutableState<Key>
}