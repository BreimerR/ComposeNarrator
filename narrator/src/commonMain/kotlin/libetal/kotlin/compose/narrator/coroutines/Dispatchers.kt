package libetal.kotlin.compose.narrator.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


expect val Dispatchers.IOCompat: CoroutineDispatcher

val Dispatchers.IO: CoroutineDispatcher
    get() = IOCompat