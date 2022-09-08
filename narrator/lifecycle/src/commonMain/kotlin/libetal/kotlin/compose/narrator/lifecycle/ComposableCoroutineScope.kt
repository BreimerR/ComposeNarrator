package libetal.kotlin.compose.narrator.lifecycle

import kotlinx.coroutines.cancel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {

    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }

}