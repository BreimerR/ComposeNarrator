package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import libetal.kotlin.compose.narrator.LocalNarrationScope
import libetal.multiplatform.log.Log
import kotlin.coroutines.CoroutineContext

interface ViewModelLifeCycleObserver : Lifecycle.Observer {
  fun onStateChangeListener(owner: LifeCycleAware)
}

abstract class ViewModel : LifeCycleAware(), ViewModelLifeCycleObserver {

    private val observers by lazy {
        mutableListOf<ViewModelLifeCycleObserver>(this)
    }

    override fun onStateChange() = observers.forEach {
        it.onStateChangeListener(this)
    }

    override fun addObserver(observer: Observer): Boolean {
        if (observer in observers) return false
        return if (observer is ViewModelLifeCycleObserver)
            observers.add(observer)
        else throw RuntimeException("ViewModel Can only use ViewModelLifeCycleObserver")
    }

    override fun removeObserver(observer: Observer): Boolean = observers.remove(observer)

    override  fun onStateChangeListener(owner: LifeCycleAware) {

        when (owner.state) {

            State.DESTROYED -> {
                onDestroy()
                coroutineScope.cancel()
                Log.d(TAG, "Destroyed ${this@ViewModel::class.qualifiedName ?: "AnonymousViewModel"}...")
            }

            State.CREATED -> {
                onCreate()
                Log.d(TAG, "Created ${this@ViewModel}...")
            }

            State.STARTED -> {
                onStart()
                Log.d(TAG, "Started ${this@ViewModel}...")
            }

            State.PAUSED -> {
                onPause()
                Log.d(TAG, "Paused ${this@ViewModel}...")
            }

            State.RESUMED -> {
                onResume()
                Log.d(TAG, "Resumed ${this@ViewModel}...")
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
    val viewModelStore = LocalNarrationScope.current.viewModelStore as ViewModelStore<Key>

    val viewModel = viewModelStore[key]

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