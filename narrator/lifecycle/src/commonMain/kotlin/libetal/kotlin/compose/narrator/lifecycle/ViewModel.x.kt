package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers

/**
 * Function is call
 **/
@Suppress("UNCHECKED_CAST")
@Composable
fun <VM : ViewModel> viewModelProvider() = LocalViewModelProvider.current as? VM

fun <T : Any> ViewModel.retrievedState(loader: suspend (callback: (T) -> Unit) -> Unit) =
    mutableStateOf<T?>(null).also {
        retrievedState(it, loader) { state, value ->
            lifeCycle.launch {
                state.value = value
            }
        }
    }

val <Key> Key.lifeCycleViewModel
    get() = NarrationViewModelStore[storeKey]

/**
 * This will return the wrong
 * viewModel during an animation change
 * thus might not be the best option to use
 **/
@Composable
@Suppress("UNCHECKED_CAST")
fun <Key, VM : ViewModel> lifeCycleViewModel(key: Key): VM = key.lifeCycleViewModel as VM

fun <T : Any> ViewModel.retrievedState(default: T, loader: suspend (callback: (T) -> Unit) -> Unit) =
    mutableStateOf(default).also { state ->
        lifeCycle.ioLaunch {
            loader {
                lifeCycle.launch(Dispatchers.Main) {
                    state.value = it
                }
            }
        }
    }

fun <T : Any> ViewModel.retrievedState(
    state: MutableState<T?>,
    loader: suspend (callback: (T) -> Unit) -> Unit,
    onFetch: (MutableState<T?>, T) -> Unit
) = lifeCycle.ioLaunch {
    loader {
        onFetch(state, it)
    }
}