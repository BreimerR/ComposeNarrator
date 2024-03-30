package libetal.kotlin.compose.narrator

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.utils.LocalActivity
import libetal.kotlin.log.info

private val LocalBackPressListener =
    compositionLocalOf<((ProgressiveNarrationScope<*, *>, ComponentActivity?) -> Unit)?> {
        null
    }

@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, ScopedComposable<ProgressiveNarrativeScope>>.() -> Unit
) {

    val owner = LocalLifecycleOwner.current

    val backStackControl: (ProgressiveNarrationScope<*, *>, ComponentActivity?) -> Unit =
        when (val backPressHandler = LocalBackPressListener.current) {
            null -> { scope, activity ->
                if (!scope.backStack.isEmpty) scope.back()

                // Non ending loop might be here be careful when changing
                if (scope.backStack.isEmpty) {
                    /*val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    activity?.startActivity(intent)*/
                    activity?.finish()
                    // TODO: This causes an exception
                } else "Narration" info "Backstack isn't empty ${scope.backStack}"
            }

            else -> { scope, a ->
                scope.back()

                if (scope.backStack.isEmpty)
                    backPressHandler(scope,a)


            }
        }

    CompositionLocalProvider(LocalBackPressListener provides backStackControl) {
        val activity = LocalActivity
        val current = LocalBackPressListener.current
        val backPressDispatcher = LocalActivity?.onBackPressedDispatcher

        NarrationJvm {
            val scope = this
            backPressDispatcher?.addCallback(owner) {
                current?.invoke(scope, activity)
            }
            prepareNarrations()
        }

    }

}
