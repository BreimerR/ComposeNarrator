package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable

class NarratorScope<Key> {

    @Composable
    fun Narrate(onNarrationEnd: () -> Boolean = { true }, prepareNarrations: NarrationScope<Key>.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        val scope = LocalNarrationScope.current as NarrationScope<Key>
        scope.backStack.addOnEmptyListener(onNarrationEnd)
        prepareNarrations(scope)
        scope.narrate()
    }

}