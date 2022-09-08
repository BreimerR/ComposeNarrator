package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable

typealias StateNarrationKey<T> = (T) -> Boolean

typealias StateComposable<T> = @Composable NarrativeScope.(T) -> Unit