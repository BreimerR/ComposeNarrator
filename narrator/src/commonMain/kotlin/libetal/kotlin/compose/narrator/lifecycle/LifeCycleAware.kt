package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.mutableStateOf
import libetal.kotlin.compose.narrator.view_models.ViewModel

abstract class LifeCycleAware : Lifecycle(), Lifecycle.Callbacks {

    val viewModel = mutableStateOf<ViewModel?>(null)

    override fun onCreate() {
        super.onCreate()
    }
}





