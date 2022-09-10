package libetal.kotlin.compose.narrator.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import libetal.kotlin.compose.narrator.NarrativeScope

interface NarrationDestination<Key : Any, Scope : NarrativeScope, C> {
    val narrationScope: NarrationScope<Key, Scope, C>
    fun narrate()
}