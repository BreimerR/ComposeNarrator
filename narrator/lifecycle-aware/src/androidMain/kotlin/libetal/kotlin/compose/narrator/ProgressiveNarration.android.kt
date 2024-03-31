package libetal.kotlin.compose.narrator

import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.compose.narrator.utils.LocalActivity
import libetal.kotlin.compose.narrator.utils.randomUUIDString
import libetal.kotlin.log.info

val LocalBackPressListener =
    compositionLocalOf<((ProgressiveNarrationScope<*, *>, ComponentActivity?) -> Unit)?> {
        null
    }

@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
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
                    backPressHandler(scope, a)


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

@Composable
actual inline fun <T : Any, reified VM : ViewModel> Narration(
    noinline vmFactory: () -> VM,
    noinline prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
) {

    val key = randomUUIDString()

    NarrationViewModelStore[key] = vmFactory

    val viewModel: VM = NarrationViewModelStore[key]


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
                    backPressHandler(scope, a)


            }
        }

    CompositionLocalProvider(LocalBackPressListener provides backStackControl) {
        val activity = LocalActivity
        val current = LocalBackPressListener.current
        val backPressDispatcher = activity?.onBackPressedDispatcher

        NarrationJvm {
            val scope = this

            key.addOnNarrationEnd {
                viewModel.pause()
            }

            backPressDispatcher?.addCallback(owner) {
                current?.invoke(scope, activity)
            }
            prepareNarrations()
        }

        LaunchedEffect(true) {
            viewModel.create()
            viewModel.resume()

            viewModel.addObserver {
                if (it == Lifecycle.State.DESTROYED) {
                    NarrationViewModelStore.invalidate(key) {
                        "LifeCycleNarration" info "Invalidated $key viewModel"
                    }
                }
            }
        }

    }

}
