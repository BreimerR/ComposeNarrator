package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable NarrativeScope.() -> Unit

typealias ScopedComposable<Scope> = @Composable Scope.() -> Unit
