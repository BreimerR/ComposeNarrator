package libetal.kotlin.compose.narrator.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

@Preview
@Composable
fun AppPreview() {
    App({}) {
        println("Close app requested")
        true
    }
}