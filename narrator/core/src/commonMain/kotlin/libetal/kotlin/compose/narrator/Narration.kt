package libetal.kotlin.compose.narrator

import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.extensions.LocalNarrationScope
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Suppress("UNCHECKED_CAST")
val <Key: Any> Key.narrative
    @Composable get() = Narrative(
        LocalNarrationScope.current as? ProgressiveNarrationScope<Key, @Composable () -> Unit>
            ?: throw RuntimeException("Failed to retrieve proper narration"),
        this
    )