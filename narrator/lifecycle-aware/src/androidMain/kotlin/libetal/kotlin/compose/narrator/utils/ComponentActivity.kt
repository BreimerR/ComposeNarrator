package libetal.kotlin.compose.narrator.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.activity.ComponentActivity


internal val LocalActivity: ComponentActivity?
    @Composable get() = when (val res = LocalContext.current) {
        is ComponentActivity -> res
        else -> null
    }
