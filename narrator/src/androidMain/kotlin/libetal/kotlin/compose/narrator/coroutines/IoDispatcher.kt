package libetal.kotlin.compose.narrator.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val IoDispatcher: CoroutineDispatcher by lazy {
    Dispatchers.IO
}