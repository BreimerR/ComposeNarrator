package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.compose.narrator.lifecycle.Lifecycle.State
import libetal.kotlin.laziest

abstract class ViewModel : LifeCycleAware<ViewModelLifecycle> {

    val coroutineScope
        get() = lifeCycle.coroutineScope

    override val lifeCycle: ViewModelLifecycle by laziest {
        ViewModelLifecycle(this)
    }

    override fun create() {
        if (!lifeCycle.wasCreated) return
        lifeCycle.state = if (lifeCycle.isPaused || lifeCycle.isDestroyed) State.CREATED
        else lifeCycle.state
    }

    companion object {
        private const val TAG = "ViewModel"
    }

}




