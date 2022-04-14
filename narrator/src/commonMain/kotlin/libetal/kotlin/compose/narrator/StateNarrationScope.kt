package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

class StateNarrationScope<T>(
    private val controlState: MutableState<T>,
    private val exitState: T,
    backStack: AdaptableNarrationBackStack,
    private val onBackPressed: () -> Boolean
) : StoryScope<StateNarrationKey, AdaptableNarrationBackStack>(backStack) {

    private val stateChangeListeners by lazy(this) {
        mutableListOf<(T) -> Unit>()
    }

    private var currentState
        get() = controlState.value
        set(value) {
            controlState.value = value
            for ((test, composable) in components) {
                if (!test()) backStack.remove(test)
            }
            stateChangeListeners.forEach { it(value) }
        }

    @Composable
    override fun narrate() {

        for ((key, component) in components) {
            if (key()) {
                component()
            }
        }

    }

    fun navigateTo(newState: T) {
        currentState = newState
    }

    override fun back(): Boolean {
        currentState = exitState
        if (backStack.isEmpty) backStack.exit()
        return onBackPressed()
    }

}