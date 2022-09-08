package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable

interface NarrationDestination<Key : Any, Invoked> {
    val narrationScope: NarrationScope<Key, Invoked, @Composable () -> Unit>
    fun narrate()
}