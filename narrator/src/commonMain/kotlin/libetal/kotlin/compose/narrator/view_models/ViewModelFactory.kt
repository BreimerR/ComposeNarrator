package libetal.kotlin.compose.narrator.view_models

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.coroutines.IoDispatcher
import libetal.kotlin.compose.narrator.lifecycle.LifeCycleAware

abstract class ViewModelFactory<VM : ViewModel> : LifeCycleAware() {

    internal val onCreateData by lazy {
        mutableMapOf<Any, MutableState<Any?>>()
    }

    private var created = false

    fun <Index : Any> create(index: Index, viewModelState: MutableState<ViewModel?>) {
        if (created) return

        ioLaunch {
            val viewModel = createViewModel()

            viewModelState.value = viewModel

            onCreateData[index]?.let {
                viewModel.onCreate(it)
            } ?: viewModel.onCreate()

        }

        created = true
    }

    abstract suspend fun createViewModel(): VM

}

fun <T> List<(T) -> Unit>.forEach(argument: T) = forEach { lambda ->
    lambda(argument)
}
