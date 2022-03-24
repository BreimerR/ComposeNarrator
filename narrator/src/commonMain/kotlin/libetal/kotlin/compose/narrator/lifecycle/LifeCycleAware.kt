package libetal.kotlin.compose.narrator.lifecycle

import kotlinx.coroutines.*
import libetal.multiplatform.log.Log

abstract class LifeCycleAware : Lifecycle(), Lifecycle.Callbacks {

    var deathDate = 5000L

    var pauseJob: Job? = null

    fun create() {
        state = if (!isPaused) State.CREATED
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

    fun pause(killAfter: Long? = deathDate) {
        state = State.PAUSED

        killAfter?.let {
            pauseJob = state.to(State.DESTROYED, it)
        }

    }

    /**
     * Wait's 5 seconds then starts
     * the fall to the target state
     **/
    fun State.to(state: State, delay: Long) = ioLaunch {

        delay(delay)

        var currentState = this@LifeCycleAware.state

        while (isActive) {
            if (currentState < this@to) {
                cancel("Death was canceled. Reversing to required state...")
                break
            }
            Log.d(TAG, "Moving to next state $currentState")

            if (this@LifeCycleAware.state == state) {

                pauseJob = null

                break
            }

            currentState = currentState.nextState

            this@LifeCycleAware.state = currentState

        }

    }


    companion object {
        const val TAG = "LifeCycleAware"
    }
}





