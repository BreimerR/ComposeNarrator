package libetal.kotlin.compose.narrator.lifecycle


import kotlinx.coroutines.*
import libetal.kotlin.debug.info
import libetal.kotlin.compose.narrator.coroutines.IO as LifeCycleIO


abstract class LifeCycleAware : Lifecycle(), Lifecycle.Callbacks {

    private var deathDate = 5000L

    private var pauseJob: Job? = null
        set(value) {

            if (field != null && value != null) return value.cancel()

            if (value == null) {
                field?.cancel()
                TAG info "Cancelling pause for ${this::class.simpleName} $state"
            }

            field = value

        }

    val wasCreated
        get() = state > State.CREATED && !isPaused && !isDestroyed

    fun create() {
        state = if (isPaused || isDestroyed) State.CREATED
        else state
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
            pauseJob = launch {
                to(state, State.DESTROYED, deathDate)
            }
        }

    }

    override fun onStateChange(state: State) = when (state) {
        State.CREATED -> {
            pauseJob = null
            TAG info "Creating... ${this::class.simpleName}"
        }
        State.RESUMED -> {
            pauseJob = null
            TAG info "Resuming... ${this::class.simpleName}: $state"
        }
        State.STARTED -> {
            TAG info "Started... ${this::class.simpleName}"
        }
        State.PAUSED -> {
            TAG info "Pausing ${this::class.simpleName}..."
        }
        State.DESTROYED -> {
            TAG info "Destroyed... ${this::class.simpleName}"
        }
    }

    /**
     * Wait's 5 seconds then starts
     * the fall to the target state
     **/
    suspend fun to(initialState: State, destinationState: State, delay: Long) = withContext(Dispatchers.LifeCycleIO) {

        delay(delay)

        val owner = this@LifeCycleAware::class.simpleName

        TAG info "$owner will be destroyed soon"

        while (state != destinationState && isActive && state >= initialState) {
            state = state.nextState
        }

    }


    companion object {
        const val TAG = "LifeCycleAware"
    }
}





