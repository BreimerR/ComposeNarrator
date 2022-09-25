package libetal.kotlin.compose.narrator.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import libetal.kotlin.compose.narrator.common.App


fun main() = application {
    Window(::exitApplication) {
        App()
    }
}