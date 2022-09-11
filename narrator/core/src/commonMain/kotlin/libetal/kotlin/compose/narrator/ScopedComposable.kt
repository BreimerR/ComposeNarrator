package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable

typealias ScopedComposable<Scope> = @Composable Scope.() -> Unit

typealias ScopedComposable1<Scope,T> = @Composable Scope.(T) -> Unit
