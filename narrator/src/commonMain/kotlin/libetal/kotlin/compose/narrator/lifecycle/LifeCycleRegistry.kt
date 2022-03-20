package libetal.kotlin.compose.narrator.lifecycle

import libetal.multiplatform.log.Log

class LifeCycleRegistry : LifeCycleAware() {

    val wasCreated: Boolean
        get() = state > State.CREATED && !isDestroyed

    private val observers = mutableListOf<LifecycleEventObserver>()

    override fun addObserver(observer: Observer): Boolean = if (observer is LifecycleEventObserver) {
        addObserver(observer)
    } else throw RuntimeException("LifeCycleRegistry can only take in LifeCycleEventObservers")


    /*If you use anonymous instances then observer will be added more than once*/
    fun addObserver(observer: LifecycleEventObserver): Boolean {
        /*Somewhere it's being added more than once using*/
        if (observer in observers) return false
        return observers.add(observer)
    }

    fun addObserver(observer: (event: Event) -> Unit) = addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(event: Event) {
            observer(event)
        }
    })

    override fun removeObserver(observer: Observer) = observers.remove(observer)

    override fun onStateChange() {
        Log.d(TAG, "Calling observers with state: $state")
        observers.forEach {
            val event = when (state) {
                State.DESTROYED -> Event.ON_DESTROY
                State.CREATED -> Event.ON_CREATE
                State.STARTED -> Event.ON_START
                State.PAUSED -> Event.ON_PAUSE
                State.RESUMED -> Event.ON_RESUME
            }

            it.onStateChanged(event)

        }
    }

    companion object {
        private val TAG = "LifeCycleRegistry"
    }

}