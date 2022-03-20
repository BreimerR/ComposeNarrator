package libetal.kotlin.compose.narrator.lifecycle

import kotlinx.coroutines.*
import libetal.kotlin.compose.narrator.coroutines.IoDispatcher
import libetal.kotlin.compose.narrator.coroutines.MainDispatcher

abstract class LifeCycleAware : Lifecycle(), Lifecycle.Callbacks {

    var deathDate = 5000L

    var pauseJob: Job? = null

    fun create() {
        state = if (!isPaused) State.DESTROYED
        else {
            pauseJob?.cancel()
            State.RESUMED
        }

    }

    /**
     * Should be called
     * is pause job is canceled
     **/
    fun resume() {
        state = State.RESUMED
    }

    fun start() {
        state = State.STARTED
    }

    fun pause() {
        state = State.PAUSED
        pauseJob = coroutineScope.to(State.DESTROYED, deathDate)
    }

    /**
     * Wait's 5 seconds then starts
     * the fall to the target state
     **/
    fun CoroutineScope.to(state: State, delay: Long) = launch(Dispatchers.Default) {

        delay(delay)

        var from = this@LifeCycleAware.state

        while (isActive) {
            if (state < this@LifeCycleAware.state) {
                cancel("Death was canceled. Reversing to required state...")
                break
            }

            if (this@LifeCycleAware.state == state) {

                pauseJob = null

                break
            }

            from = from.nextState

            this@LifeCycleAware.state = from

        }

    }


}





