package libetal.kotlin.compose.narrator

typealias StateNarrationKey<T> = (T) -> Boolean

typealias StateComposable<T> = ScopedComposable1<StateNarrativeScope, T>