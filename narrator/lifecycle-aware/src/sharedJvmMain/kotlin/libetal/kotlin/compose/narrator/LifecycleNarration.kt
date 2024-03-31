package libetal.kotlin.compose.narrator

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.interfaces.MutableStateNarrationScope
import libetal.kotlin.compose.narrator.interfaces.SnapShotStateNarrationScope
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.NarrationViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.log.debug
import libetal.kotlin.log.info

val <Key : Any> Key.viewModelStoreKey
    get() = toString()

@Suppress("UNCHECKED_CAST")
inline operator fun <Key : Any, reified VM : ViewModel, NScope : NarrativeScope> Key.invoke(
    scope: ProgressiveNarrationScope<Key, @Composable NScope.() -> Unit>,
    noinline vmFactory: () -> VM,
    noinline content: @Composable NScope.(VM) -> Unit
) = with(scope) {

    val key = this@invoke.viewModelStoreKey

    NarrationViewModelStore[key] = vmFactory

    add(this@invoke) {

        val viewModel: VM = NarrationViewModelStore[key]

        content(viewModel)

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

        addOnNarrationEnd {
            viewModel.pause()
        }

    }

}

@Suppress("UNCHECKED_CAST")
inline operator fun <T, reified VM : ViewModel> String.invoke(
    scope: MutableStateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>,
    noinline vmFactory: () -> VM,
    noinline content: @Composable MutableStateNarrationScope<T, @Composable StateNarrativeScope.(T) -> Unit>.(VM) -> Unit
) = with(scope) {

    val key = this@invoke.viewModelStoreKey

    NarrationViewModelStore[key] = vmFactory

    add(this@invoke) {

        val viewModel: VM = NarrationViewModelStore[key]

        content(viewModel)

        LaunchedEffect(true) {
            viewModel.create()
            viewModel.resume()

            viewModel.addObserver {
                if (it == Lifecycle.State.DESTROYED) {
                    NarrationViewModelStore.invalidate(key) {

                    }
                }
            }
        }

        addOnNarrationEnd {
            viewModel.pause()
        }

    }

}


@Suppress("UNCHECKED_CAST")
inline operator fun <T, reified VM : ViewModel> String.invoke(
    scope: SnapShotStateNarrationScope<T, @Composable StateNarrativeScope.(SnapshotStateList<T>) -> Unit>,
    noinline vmFactory: () -> VM,
    noinline content: @Composable SnapShotStateNarrationScope<T, @Composable StateNarrativeScope.(SnapshotStateList<T>) -> Unit>.(VM) -> Unit
) = with(scope) {

    val key = this@invoke.viewModelStoreKey

    NarrationViewModelStore[key] = vmFactory

    add(this@invoke) {

        val viewModel: VM = NarrationViewModelStore[key]

        content(viewModel)

        LaunchedEffect(true) {
            viewModel.create()
            viewModel.resume()

            viewModel.addObserver {
                if (it == Lifecycle.State.DESTROYED) {
                    NarrationViewModelStore.invalidate(key) {

                    }
                }
            }
        }

        addOnNarrationEnd {
            viewModel.pause()
        }

    }

}

val LocalNarrationKeyId = compositionLocalOf<String?> { null }

val currentScopeUUID
    @Composable get() = LocalNarrationKeyId.current ?: throw RuntimeException("Only Used in viewModel Narrations")

@Composable
@Deprecated("Use wasn't validated")
inline fun <Key : Any, reified VM : ViewModel> Narration(
    scopeBuilder: (uuid: String, stack: SnapshotStateList<Key>) -> ProgressiveNarrationScope<Key, @Composable ProgressiveNarrativeScope.() -> Unit>,
    noinline vmFactory: () -> VM,
    noinline prepareNarratives: ProgressiveNarrationScope<Key, @Composable ProgressiveNarrativeScope.() -> Unit>.(viewModel: VM, uuid: String) -> Unit
) {

    val uuid = "${prepareNarratives.hashCode()}"
    val scope = scopeBuilder(
        uuid,
        remember { mutableStateListOf() }
    )

    NarrationViewModelStore[scope.uuid] = vmFactory

    LocalNarrationScope.current?.addChild(scope)
    for (collector in scopeCollectors) {
        collector collect scope
    }
    scopeCollectors.clear()

    CompositionLocalProvider(
        LocalNarrationScope provides scope,
        LocalNarrationKeyId provides uuid
    ) {

        val vm:VM = NarrationViewModelStore[scope.uuid]

        with(scope) {
            @Suppress("UNCHECKED_CAST")
            prepareNarratives(vm, uuid)
            Narrate()
        }

        LaunchedEffect(true) {

            vm.resume()

            vm.addObserver {
                if (it == Lifecycle.State.DESTROYED) {
                    NarrationViewModelStore.invalidate(scope.uuid) {
                        "LifecycleNarration" debug "Invalidated ${vm::class}"
                    }
                }
            }

        }

        DisposableEffect(vm) {

            onDispose {
                vm.pause()
            }

        }

    }

}
