package libetal.kotlin.compose.narrator.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import libetal.kotlin.compose.narrator.common.App

fun main(vararg args: String) = application {
    Window(::exitApplication, title = "Narrator") {
        App()
    }
}