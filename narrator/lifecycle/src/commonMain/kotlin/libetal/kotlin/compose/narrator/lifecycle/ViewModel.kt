package libetal.kotlin.compose.narrator.lifecycle

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle.State
import libetal.kotlin.coroutines.IO
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

    fun launch(suspendedFun: suspend () -> Unit) = coroutineScope.launch(Dispatchers.Main) {
        suspendedFun()
    }

    fun ioLaunch(suspendedFun: suspend () -> Unit) = coroutineScope.launch(Dispatchers.IO) {
        suspendedFun()
    }

    companion object {
        private const val TAG = "ViewModel"
    }

}




