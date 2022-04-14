package libetal.kotlin.compose.narrator.common

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.debug.info


class HelloWorldViewModel : ViewModel() {

    init {
        TAG info "Init"
    }

    val messageState = mutableStateOf("")

    val countState = mutableStateOf(0)

    val persistentState = mutableStateOf(0)

    override fun onCreate() {
        messageState.value = "Hello: OnCreate"
        coroutineScope.launch(Dispatchers.IO) {
            repeat(1000) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    persistentState.value += 1
                }
            }
        }
        TAG info "onCreate"
    }

    override fun onResume() {
        messageState.value = "Hello: onResume"
        countState.value = 0
        pauseJob = null
        TAG info "onResume"
    }

    private var pauseJob: Job? = null
        set(value) {
            if (value == null) field?.cancel()

            field = value

        }

    override fun onPause() {
        messageState.value = "Hello: onPause"
        pauseJob = coroutineScope.launch(Dispatchers.IO) {
            repeat(1000) {
                countState.value += 1
                delay(1000)
            }
        }
        TAG info "onPause"
    }

    override fun onStart() {
        messageState.value = "Hello: onStart"
        TAG info "onStart"
    }

    override fun onDestroy() {
        messageState.value = "Hello: onDestroy"
        TAG info "onDestroy"
    }

    companion object {
        const val TAG = "HelloWorldViewModel"
    }

}