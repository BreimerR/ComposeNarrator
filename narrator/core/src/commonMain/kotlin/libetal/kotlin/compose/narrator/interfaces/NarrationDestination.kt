package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable

interface NarrationDestination<Key> {
    val narrationScope: NarrationScope<Key, @Composable () -> Unit>
    fun narrate()
}