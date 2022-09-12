package libetal.narrator

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import libetal.kotlin.compose.narrator.ProgressiveNarrativeScope
import libetal.kotlin.compose.narrator.ScopedComposable
import libetal.kotlin.compose.narrator.common.App
import libetal.kotlin.compose.narrator.common.AppNarrations
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope


fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Narrator") {

        createScopeCollector<ProgressiveNarrationScope<AppNarrations, ScopedComposable<ProgressiveNarrativeScope>>> {
            addOnNarrationEnd {
                exitApplication()
            }
        }

        MaterialTheme {
            App()
        }

    }

}