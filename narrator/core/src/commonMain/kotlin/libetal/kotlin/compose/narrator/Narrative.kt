package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.NarrationDestination
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

class Narrative<Key:Any>(override val narrationScope: ProgressiveNarrationScope<Key,@Composable ()->Unit>, private val destination: Key) : NarrationDestination<Key> {
    override fun narrate() = narrationScope.backStack.navigateTo(destination)
}

