package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.compose.narrator.utils.randomUUIDString
import libetal.kotlin.log.info


@Composable
actual fun <T : Any> Narration(
    prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
): Unit = NarrationJvm(prepareNarrations)

@Composable
actual inline fun <T : Any, reified VM : ViewModel> Narration(
    noinline vmFactory: () -> VM,
    noinline prepareNarrations: ProgressiveNarrationScope<T, @Composable ProgressiveNarrativeScope.() -> Unit>.() -> Unit
) {
    val key = randomUUIDString()

    NarrationViewModelStore[key] = vmFactory

    val viewModel: VM = NarrationViewModelStore[key]

    NarrationJvm{
        key.addOnNarrationEnd{
            viewModel.pause()
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
