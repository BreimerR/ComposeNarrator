package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.compose.narrator.lifecycle.ViewModelStore
import libetal.kotlin.debug.info

val <Key : Any> Key.viewModelStoreKey
    get() = toString()

@Composable
@Suppress("UNCHECKED_CAST")
fun <Key : Any, VM : ViewModel> Key.invoke(
    vmFactory: () -> ViewModel, content: @Composable NarrativeScope.(VM) -> Unit
) = with(
    (LocalNarrationScope.current as? NarrationScopeImpl<Key>) ?: throw RuntimeException("Can't be run outside a composable scope")
) {
    val key = this@invoke.viewModelStoreKey
    NarrationViewModelStore[key] = vmFactory

    add(this@invoke) {

        val viewModel = NarrationViewModelStore[key] as VM

        viewModel.create()

        content(viewModel)

        LaunchedEffect(viewModel) {
            viewModel.resume()
        }

        addOnExitRequest {
            viewModel.addObserver {
                if (it == Lifecycle.State.DESTROYED) {
                    NarrationViewModelStore.invalidate(key) {
                        "${viewModel::class}" info "Invalidated the viewModel"
                    }
                }

            }

            viewModel.pause()

            true
        }

    }

}