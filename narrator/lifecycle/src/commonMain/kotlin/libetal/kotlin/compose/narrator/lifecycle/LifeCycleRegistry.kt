package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.compose.narrator.lifecycle.Lifecycle.State
import libetal.kotlin.laziest

class LifeCycleRegistry : LifeCycleAware<RegistryLifecycle> {

    private val observers by laziest {
        mutableListOf<LifecycleEventObserver>()
    }

    override val lifeCycle: RegistryLifecycle by laziest {
        RegistryLifecycle()
    }

    /*override fun addObserver(observer: Lifecycle.Observer): Boolean = if (observer is LifecycleEventObserver) {
        addObserver(observer)
    } else throw RuntimeException("LifeCycleRegistry can only take in LifeCycleEventObservers")*/

    /*If you use anonymous instances then observer will be added more than once*/
    fun addObserver(observer: LifecycleEventObserver): Boolean {
        /*Somewhere it's being added more than once using*/
        if (observer in observers) return false
        return observers.add(observer)
    }

    /*un addObserver(observer: (event: Lifecycle.Event) -> Unit) = addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(event: Event) {
            observer(event)
        }
    })*/

    fun removeObserver(observer: Lifecycle.Observer) = observers.remove(observer)

    fun onStateChange(state: State) {
        observers.forEach {
            it.onStateChanged(state)
        }
    }

    companion object {
        private const val TAG = "LifeCycleRegistry"
    }


}