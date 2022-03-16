package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.multiplatform.log.Log


val LocalNarrationScope by compositionProvider<NarrationScope<*>>()

val <Key : Enum<*>> Key.NavigationController: NarrationScope<Key>
    @Composable get() {
        val controller = LocalNarrationScope.current

        return try {
            @Suppress("UNCHECKED_CAST")
            controller as NarrationScope<Key>
        } catch (e: ClassCastException) {
            /**TODO
             * Lead users to the component
             **/
            throw RuntimeException("Key used can't is not available in this composable's compositionProvider $this.")
        }

    }


/**
 * This cast might
 * fail but not sure where yet
 * and a cause for it to fail as goal
 * is to implement a tight typeCheck
 * */
val <Key : Enum<*>> Key.narration
    @Composable get() = Narration(this, LocalNarrationScope.current as NarrationScope<Key>)

val historyScope
    @Composable get() = History(LocalNarrationScope.current as NarrationScope<Any>)

class Narration<Key>(private val key: Key, override val scope: NarrationScope<Key>) : NarrationDestination<Key> {
    override fun begin() = scope.navigateTo(key)
}

class History(override val scope: NarrationScope<Any>) : NarrationHistory {
    override fun begin(onEmpty: () -> Boolean): Boolean = scope.back(onEmpty)
}