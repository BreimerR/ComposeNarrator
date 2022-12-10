package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.log.debug
import libetal.kotlin.log.info
import libetal.kotlin.name

class ViewModelLifecycle(private val owner: Callbacks, killDuration: Long = 5000L) : Lifecycle(killDuration) {

    override fun onStateChange(state: State) {

        TAG info "onStateChangeListener: ${this::class.simpleName} state: $state"

        val className = owner::class.name ?: TAG

        when (state) {

            State.DESTROYED -> owner.onDestroy()

            State.CREATED -> owner.onCreate()

            State.PAUSED -> owner.onPause()

            State.RESUMED -> owner.onResume()

        }

        className debug state.name

        super.onStateChange(state)

    }

    companion object {
        const val TAG = "ViewModelLifecycle"
    }
}