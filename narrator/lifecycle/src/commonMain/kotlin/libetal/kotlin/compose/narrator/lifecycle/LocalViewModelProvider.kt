package libetal.kotlin.compose.narrator.lifecycle

import androidx.compose.runtime.compositionLocalOf

val LocalViewModelProvider = compositionLocalOf<ViewModel?> { null }