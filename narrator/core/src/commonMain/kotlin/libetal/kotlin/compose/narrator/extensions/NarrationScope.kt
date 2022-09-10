package libetal.kotlin.compose.narrator.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import libetal.kotlin.compose.narrator.NarrativeScope
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

val LocalNarrationScope =
    compositionLocalOf<NarrationScope<out Any, out NarrativeScope, *>?> { null }