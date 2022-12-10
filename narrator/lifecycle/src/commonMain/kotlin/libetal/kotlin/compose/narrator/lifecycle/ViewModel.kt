package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle.State
import libetal.kotlin.coroutines.IO
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

    fun <T> SnapshotStateList<T>.fetch(builder: suspend FlowCollector<T>.() -> Unit): SnapshotStateList<T> =
        also { state ->

            val flow = flow {
                ioLaunch {
                    builder()
                }
            }

            launch {
                flow.collectLatest {
                    state.add(it)
                }
            }

        }


    fun <T> MutableState<T>.fetch(builder: suspend MutableState<T>.() -> T) = also { state ->
        ioLaunch {
            val value = builder()
            launch {
                state.value = value
            }
        }
    }

    companion object {
        private const val TAG = "ViewModel"
    }


}




