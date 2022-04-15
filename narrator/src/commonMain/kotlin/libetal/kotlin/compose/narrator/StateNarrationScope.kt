package libetal.kotlin.compose.narrator

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.lifecycle.StoryViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel

class StateNarrationScope<T>(
    backStack: StateNarrationBackStack<T>,
    private val controlState: MutableState<T>,
    private val exitState: T
) : StoryScope<StateNarrationKey<T>, StateNarrationBackStack<T>>(backStack) {

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

    @Composable
    operator fun <VM : ViewModel> (StateNarrationKey<T>).invoke(
        viewModelProvider: () -> VM,
        content: @Composable VM.(StateNarrationKey<T>) -> Unit
    ) {

        if (invoke(controlState.value)) {

            if (StoryViewModelStore[storeKey] == null) StoryViewModelStore[storeKey] = viewModelProvider

            val viewModel = lifeCycleViewModel<VM>()

            if (!viewModel.wasCreated) viewModel.create()


            content(lifeCycleViewModel(), this)

            LaunchedEffect(viewModel) {
                viewModel.resume()
            }

            DisposableEffect(viewModel) {
                onDispose {
                    viewModel.pause()
                }
            }
        }

    }

    @Composable
    operator fun (StateNarrationKey<T>).invoke(content: @Composable StateNarrationKey<T>.() -> Unit) {
        if (this(controlState.value)) {
            content()
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