package libetal.kotlin.compose.narrator.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

val LocalNarrationScope = compositionLocalOf<NarrationScope<*, *>?> { null }

@Composable
fun <Key, Scope : NarrationScope<Key, *>> withNarrationScope(content: @Composable Scope.() -> Unit) {
    @Suppress("UNCHECKED_CAST")
    val scope = LocalNarrationScope.current as? Scope
        ?: throw RuntimeException("You need to call Narration before requesting for a scope")

    content(scope)

}