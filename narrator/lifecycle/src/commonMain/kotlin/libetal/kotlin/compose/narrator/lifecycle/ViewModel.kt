package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.compose.narrator.lifecycle.Lifecycle.State
import libetal.kotlin.debug.info
import libetal.kotlin.laziest

abstract class ViewModel(killTime: Long = 5000L) : LifeCycleAware<ViewModelLifecycle> {

    val coroutineScope
        get() = lifeCycle.coroutineScope

    override val lifeCycle: ViewModelLifecycle by laziest {
        ViewModelLifecycle(this, killTime)
    }

    override fun create() {
        if (!lifeCycle.wasCreated && !lifeCycle.isPausing) {
            lifeCycle.state = if (lifeCycle.isPaused || lifeCycle.isDestroyed) State.CREATED
            else lifeCycle.state
        }
    }

    fun addObserver(observer: (State) -> Unit) = lifeCycle.addObserver(observer)

    companion object {
        private const val TAG = "ViewModel"
    }

}




