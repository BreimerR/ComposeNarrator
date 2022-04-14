package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import libetal.kotlin.compose.narrator.LocalNarrationScope
import libetal.kotlin.debug.debug
import libetal.kotlin.debug.info
import kotlin.coroutines.CoroutineContext

interface ViewModelLifeCycleObserver : Lifecycle.Observer {
    fun onStateChangeListener(owner: LifeCycleAware, state: Lifecycle.State)
}

abstract class ViewModel : LifeCycleAware(), ViewModelLifeCycleObserver {

    private val observers by lazy {
        mutableListOf<ViewModelLifeCycleObserver>(this)
    }

    override fun onStateChange(state: State) {
        super.onStateChange(state)

        try {
            observers.forEach {
                it.onStateChangeListener(this, state)
                TAG info "Calling state change for ${this::class.simpleName} $state"
            }
        } catch (e: ConcurrentModificationException) {
            TAG info "Really hard to sort this out"
        }

    }

    override fun addObserver(observer: Observer): Boolean {
        if (observer in observers) return false
        return if (observer is ViewModelLifeCycleObserver)
            observers.add(observer)
        else throw RuntimeException("ViewModel Can only use ViewModelLifeCycleObserver")
    }

    override fun removeObserver(observer: Observer): Boolean = observers.remove(observer)

    override fun onStateChangeListener(owner: LifeCycleAware, state: State) {
        TAG info "onStateChangeListener: ${owner::class.simpleName} state: ${owner.state}"
        when (state) {

            State.DESTROYED -> {
                onDestroy()
                coroutineScope.cancel()
                TAG debug "Destroyed $state ${this@ViewModel::class.qualifiedName ?: "AnonymousViewModel"}..."
            }

            State.CREATED -> {
                onCreate()
                TAG info "Created ${this@ViewModel}..."
            }

            State.STARTED -> {
                onStart()
                TAG info "Started ${this@ViewModel}..."
            }

            State.PAUSED -> {
                onPause()
                TAG info "Paused ${this@ViewModel}..."
            }

            State.RESUMED -> {
                onResume()
                TAG info "Resumed ${this@ViewModel}..."
            }
        }

    }

    companion object {
        private const val TAG = "ViewModel"
    }

}

internal val LocalViewModelProvider = compositionLocalOf<ViewModel?> { null }


/**
 * Function is call
 **/
@Suppress("UNCHECKED_CAST")
@Composable
fun <VM : ViewModel> viewModelProvider() = LocalViewModelProvider.current as? VM

/**
 * This will return the wrong
 * viewModel during an animation change
 * thus might not be the best option to use
 **/
@Composable
fun <Key, VM : ViewModel> lifeCycleViewModel(key: Key): VM {

    val viewModel = StoryViewModelStore["${key!!::class.qualifiedName!!}.$key"]

    return try {
        @Suppress("UNCHECKED_CAST")
        viewModel as VM
    } catch (e: Exception) {
        throw NullPointerException("The composable you are requesting a viewModel for isn't inside the current narration key.")
    }

}

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {

    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }

}

interface Closeable {
    fun close()
}