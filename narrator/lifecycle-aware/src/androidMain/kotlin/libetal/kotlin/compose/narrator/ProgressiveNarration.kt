package libetal.kotlin.compose.narrator

import androidx.activity.addCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.utils.LocalActivity
import libetal.kotlin.log.info


@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit
): Unit {

    val owner = LocalLifecycleOwner.current
    val activity = LocalActivity
    val backPressDispatcher = activity?.onBackPressedDispatcher

    // TODO handle back pressed better for android
    NarrationJvm {
        val scope = this
        backPressDispatcher?.addCallback(owner) {
            scope.back()// Non ending loop might be here
            if (scope.backStack.isEmpty)
                "Narration" info "Back stack is empty ${scope.backStack}"
            else
                "Narration" info "Backstack isn't empty ${scope.backStack}"
        }
        prepareNarrations()
    }

}
