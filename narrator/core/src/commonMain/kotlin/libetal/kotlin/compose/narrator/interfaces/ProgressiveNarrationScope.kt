package libetal.kotlin.compose.narrator.interfaces

import libetal.kotlin.debug.debug
import libetal.kotlin.debug.info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import libetal.kotlin.compose.narrator.ProgressiveNarrativeScope
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.listeners.ExitRequestListener

interface ProgressiveNarrationScope<Key : Any, C> : NarrationScope<Key, ProgressiveNarrativeScope, C> {

    val backStack: ListBackStack<Key>

    override val prevKey: Key?
        get() = backStack.previous

    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty

    val currentKey: Key
        get() = backStack.current

    val currentComponent
        get() = composables[currentKey]

    val currentNarrativeScope: ProgressiveNarrativeScope
        get() = narrativeScopes[currentKey] ?: newNarrativeScope.also {
            narrativeScopes[currentKey] = it
        }

    /**
     * Adds a view to the current
     * composition backStack
     **/
    override fun add(key: Key, content: C) {
        key.addToBackstack()
        composables[key] = content
    }

    override val newNarrativeScope
        get() = ProgressiveNarrativeScope()

    fun Key.addToBackstack() {
        if (backStack.isEmpty) backStack.add(this)
    }

    /**
     * TODO
     * Navigates away from
     * the current narrative
     * There is need to clean up a few places
     **/
    fun Key.narrate(removeCurrentFromBackStack: Boolean = false) {
        if (currentNarrativeScope.hasCliffhangers) {
            val previous = currentKey
            val existedInStack = backStack.navigateTo(this)
            if (existedInStack || removeCurrentFromBackStack) {
                backStack.invalidate(previous)
                cleanUp(previous)
            }
        }
    }

    @Composable
    override fun Narrate() = when (val composable: C? = currentComponent) {
        null -> throw RuntimeException("Failed to retrieve component")
        else -> Narrate(composable)
    }

    private fun runNarrativeExitRequestListeners(shouldExit: Boolean): Boolean {
        var exit = shouldExit

        val listeners = onNarrativeExitRequest[currentKey]

        if (listeners != null) {
            for (listener in listeners) {
                exit = exit && listener(this)
                if (!exit) return false
            }
        }

        return exit
    }


    @Composable
    fun Narrate(composable: C) {

        Compose(composable)

        DisposableEffect(currentKey) {

            onDispose {

                val key = prevKey ?: return@onDispose

                NarrationScope.TAG debug "Disposing $key"

                cleanUp(key)

                val listeners = onNarrationEndListeners[key] ?: return@onDispose

                for (listener in listeners) {
                    listener()
                }

            }

        }

    }

    fun onCurrentKeyExitRequestListener(action: (NarrationScope<Key, ProgressiveNarrativeScope, C>) -> Boolean) {
        currentKey.addOnNarrativeExitRequest(action)
    }

    override fun back(): Boolean {

        var shouldExit = runNarrativeExitRequestListeners(true)

        if (!shouldExit) return false

        for ((_, child) in children) {
            shouldExit = shouldExit && child.back()
            if (!shouldExit) return false
        }


        if (super.back()) backStack.pop()

        return backStack.isEmpty

    }

    fun ProgressiveNarrativeScope.addOnExitRequest(action: ExitRequestListener) =
        addOnExitRequest(this@ProgressiveNarrationScope, action)

    companion object {
        const val TAG = "ProgressiveNarrationScope"
    }

}



