package libetal.kotlin.compose.narrator.view_models

import androidx.compose.runtime.MutableState
import libetal.kotlin.compose.narrator.lifecycle.LifeCycleAware

abstract class ViewModelFactory<VM : ViewModel> : LifeCycleAware() {

    internal val onCreateData by lazy {
        mutableMapOf<Any, MutableState<Any?>>()
    }

    private var viewModelState: MutableState<ViewModel?>? = null

    private var created = false

    val onCreateObservers = mutableListOf<(Any?) -> Unit>()

    var onCreateObserver: (Any) -> Unit = {}

    fun <Index : Any> create(index: Index, newViewModelState: MutableState<ViewModel?>) {
        if (created) return

        ioLaunch {

            viewModelState = newViewModelState

            viewModelState?.apply {
                value = createViewModel()
            }

            created = true

            onCreateData[index]?.value?.let { data ->
                onCreateObserver(data)
            }

        }


    }

    internal fun <D> updateViewModelData(data: D) = ioLaunch {
        if (created) viewModelState?.value?.onCreateData?.value = data
        else onCreateObserver = {
            viewModelState?.value?.onCreateData?.value = data
        }
    }

    abstract suspend fun createViewModel(data: Any? = null): VM

}
