package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable

typealias StateNarrationComposable<T> = @Composable StateNarrativeScope.(T) -> Unit