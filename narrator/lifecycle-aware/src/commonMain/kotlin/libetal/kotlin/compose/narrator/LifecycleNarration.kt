package libetal.kotlin.compose.narrator

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.debug.info

val <Key : Any> Key.viewModelStoreKey
    get() = toString()

@Suppress("UNCHECKED_CAST")
operator fun <Key : Any, VM : ViewModel, NScope : NarrativeScope> Key.invoke(
    scope: NarrationScope<Key, NScope, ScopedComposable<NScope>>,
    vmFactory: () -> VM,
    content: @Composable NScope.(VM) -> Unit
) = with(scope) {

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

@Composable
fun <Key : Any, VM : ViewModel> Narration(
    scopeBuilder: (uuid: String, stack: SnapshotStateList<Key>) -> ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>,
    vmFactory: () -> VM,
    prepareNarratives: ProgressiveNarrationScope<Key, ScopedComposable<ProgressiveNarrativeScope>>.(VM) -> Unit
) {
    val scope = scopeBuilder(
        "${prepareNarratives.hashCode()}",
        remember { mutableStateListOf() }
    )

    NarrationViewModelStore[scope.uuid] = vmFactory

    scope.addOnNarrationEnd {
        val vm = NarrationViewModelStore[scope.uuid]
        vm.pause()
    }

    CompositionLocalProvider(LocalNarrationScope provides scope) {

        val vm = NarrationViewModelStore[scope.uuid]

        with(scope) {
            @Suppress("UNCHECKED_CAST")
            prepareNarratives(vm as VM)
            Narrate()
        }

        LaunchedEffect(scope) {
            vm.resume()
        }

    }

}