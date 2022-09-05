package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope


/**
 * @param backHandler
 * If provided handles
 **/
@Composable
fun <Key : Any> Narration(prepareComponents: ProgressiveNarrationScope<Key, ComposableFun>.() -> Unit) {
    val backStack = NarrationBackStack(
        remember { mutableStateListOf<Key>() }
    )
    val scope = NarrationScopeImpl(backStack)
    CompositionLocalProvider(LocalNarrationScope provides scope) {
        with(scope) {
            prepareComponents()
            Narrate()
        }
    }
}