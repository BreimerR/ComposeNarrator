package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable

interface NarrationDestination<Key : Any> {
    val narrationScope: NarrationScope<Key, @Composable () -> Unit>
    fun narrate()
}