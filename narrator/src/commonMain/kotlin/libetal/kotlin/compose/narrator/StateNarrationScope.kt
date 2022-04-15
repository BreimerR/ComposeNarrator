package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import libetal.kotlin.debug.debug

class StateNarrationScope<T>(
    backStack: AdaptableNarrationBackStack<T>,
    private val controlState: MutableState<T>,
    private val exitState: T
) : StoryScope<StateNarrationKey<T>, AdaptableNarrationBackStack<T>>(backStack) {

    override val shouldExit: Boolean
        get() = currentState == exitState

    private val stateChangeListeners by lazy(this) {
        mutableListOf<(T) -> Unit>()
    }

    private var currentState
        get() = controlState.value
        set(value) {
            controlState.value = value
            for ((test, composable) in components) {
                if (!test(controlState.value)) backStack.remove(test)
            }
            stateChangeListeners.forEach { it(value) }
        }

    @Composable
    override fun narrate() {

        for ((key, component) in components) {
            if (key(controlState.value)) {
                component()
            }
        }

    }

    fun navigateTo(newState: T) {
        currentState = newState
    }

    override fun back(): Boolean = if (shouldExit) {
        super.backStack.exit()
    } else {
        currentState = exitState
        false
    }

    companion object {
        private const val TAG = "StateNarrationScope"
    }

}