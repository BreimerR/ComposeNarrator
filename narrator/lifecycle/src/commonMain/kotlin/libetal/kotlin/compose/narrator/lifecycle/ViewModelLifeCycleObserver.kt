package libetal.kotlin.compose.narrator.lifecycle

interface ViewModelLifeCycleObserver<L : Lifecycle> : Lifecycle.Observer {
    fun onStateChangeListener(owner: Lifecycle.Owner<L>, state: Lifecycle.State) {
        onStateChange(state)
    }
}