package libetal.kotlin.compose.narrator


import android.content.Intent
import androidx.activity.addCallback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.utils.LocalActivity
import libetal.kotlin.log.info


@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit
) {

    val owner = LocalLifecycleOwner.current
    val activity = LocalActivity
    val backPressDispatcher = activity?.onBackPressedDispatcher

    // TODO handle back pressed better for android
    NarrationJvm {
        val scope = this
        backPressDispatcher?.addCallback(owner) {
            scope.back()// Non ending loop might be here
            if (scope.backStack.isEmpty) {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                activity.startActivity(intent)
            } else "Narration" info "Backstack isn't empty ${scope.backStack}"
        }
        prepareNarrations()
    }

}
