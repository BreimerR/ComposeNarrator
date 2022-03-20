package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.*
import libetal.multiplatform.log.Log
import kotlin.coroutines.CoroutineContext

interface ViewModelLifeCycleObserver : Lifecycle.Observer {
    fun onStateChangeListener()
}

abstract class ViewModel : LifeCycleAware(), ViewModelLifeCycleObserver {

    private val observers by lazy {
        mutableListOf<ViewModelLifeCycleObserver>(this)
    }

    override fun onStateChange() = observers.forEach {
        it.onStateChangeListener()
    }

    override fun addObserver(observer: Observer): Boolean {
        if (observer in observers) return false
        return if (observer is ViewModelLifeCycleObserver)
            observers.add(observer)
        else throw RuntimeException("ViewModel Can only use ViewModelLifeCycleObserver")
    }

    override fun removeObserver(observer: Observer): Boolean = observers.remove(observer)

    override fun onStateChangeListener() {

        when (state) {

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
fun <VM : ViewModel> lifeCycleViewModel(): VM {
    val viewModel = viewModelProvider<VM>()
    return try {
        @Suppress("UNCHECKED_CAST")
        viewModel as VM
    } catch (e: Exception) {
        throw NullPointerException("Make sure correct key invoke was used operator Key.invoke(provider:()->ViewModel) was used")
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