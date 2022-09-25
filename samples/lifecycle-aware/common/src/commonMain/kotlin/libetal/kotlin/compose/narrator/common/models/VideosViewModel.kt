package libetal.kotlin.compose.narrator.common.models

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.lifecycle.ViewModel

class VideosViewModel : ViewModel() {

    val countState = mutableStateOf(0)
    val allowExitState = mutableStateOf(false)

    private var count
        get() = countState.value
        set(value) {
            countState.value = value
        }

    override fun onCreate() {
        allowExitState.value = true
        coroutineScope.launch(Dispatchers.IO) {
            while (isActive) {
                delay(1000)
                launch(Dispatchers.Main) {
                    count++
                }
            }
        }
    }


}