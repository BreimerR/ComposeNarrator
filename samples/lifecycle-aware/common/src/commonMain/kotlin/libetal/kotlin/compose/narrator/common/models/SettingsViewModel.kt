package libetal.kotlin.compose.narrator.common.models

import libetal.kotlin.compose.narrator.common.data.UserState
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.log.info


class SettingsViewModel : ViewModel(Long.MAX_VALUE) {


    val userState = UserState()

    val user by userState

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
