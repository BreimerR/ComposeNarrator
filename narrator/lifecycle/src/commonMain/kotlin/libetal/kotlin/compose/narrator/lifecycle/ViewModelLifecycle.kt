package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.debug.debug
import libetal.kotlin.debug.info
import libetal.kotlin.name

class ViewModelLifecycle(val owner: Lifecycle.Callbacks) : Lifecycle() {

    override fun onStateChange(state: State) {

        TAG info "onStateChangeListener: ${this::class.simpleName} state: $state"

        when (state) {

            State.DESTROYED -> {
                owner.onDestroy()
                TAG debug "Destroyed $state ${this::class.name ?: "AnonymousViewModel"}..."
            }

            State.CREATED -> {
                owner.onCreate()
                TAG info "Created ${this}..."
            }

            State.STARTED -> {
                owner.onStart()
                TAG info "Started ${this}..."
            }

            State.PAUSED -> {
                owner.onPause()
                TAG info "Paused ${this}..."
            }

            State.RESUMED -> {
                owner.onResume()
                TAG info "Resumed ${this}..."
            }
        }

        super.onStateChange(state)

    }

    companion object {
        const val TAG = "ViewModelLifecycle"
    }
}