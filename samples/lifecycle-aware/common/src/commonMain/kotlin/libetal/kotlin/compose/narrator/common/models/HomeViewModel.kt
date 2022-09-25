package libetal.kotlin.compose.narrator.common.models

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.common.data.User
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.debug.info

class HomeViewModel : ViewModel(Long.MAX_VALUE) {


    val userState = mutableStateOf<User?>(null)


    override fun onResume() {
        TAG info "Home view model resuming"
    }


    override fun onPause() {
        TAG info "HomeViewModel Pausing"
    }

    override fun onDestroy() {
        TAG info "HomeViewModel Destroyed"
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}