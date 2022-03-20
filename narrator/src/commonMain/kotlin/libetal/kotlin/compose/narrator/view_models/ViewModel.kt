package libetal.kotlin.compose.narrator.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import libetal.kotlin.compose.narrator.lifecycle.LifeCycleAware

abstract class ViewModel : LifeCycleAware() {

    val onCreateData by lazy {
        mutableStateOf<Any?>(null)
    }

    open fun onCreate(data: MutableState<Any?>) {
        onCreate()
    }

}
