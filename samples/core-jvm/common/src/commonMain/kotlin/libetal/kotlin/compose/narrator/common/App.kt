package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.narrative

@Composable
fun App(scopeProvider: (NarrationScope<*, @Composable () -> Unit>) -> Unit) =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Narration<AppNarrations> {
            scopeProvider(this)

            AppNarrations.HOME {
                val settingsNarrative = AppNarrations.SETTINGS.narrative
                CardedComponent(4.dp) {
                    Text("Home")
                    Button({
                        settingsNarrative.narrate()
                    }) {
                        Text("Settings")
                    }
                }
            }

            AppNarrations.SETTINGS({ scope -> true }) {

                val videosNarrative = AppNarrations.VIDEOS.narrative
                CardedComponent(4.dp) {
                    Text("Settings View")
                    Button({
                        videosNarrative.narrate()
                    }) {
                        Text("Videos")
                    }
                }
            }

            AppNarrations.VIDEOS narrates {
                val settingsNarrative = AppNarrations.SETTINGS.narrative

                CardedComponent(4.dp) {
                    Text("Videos")
                    Button({
                        settingsNarrative.narrate()
                    }) {
                        Text("Settings")
                    }
                }

                addOnExitRequest {
                    false
                }

            }

        }
    }


@Composable
fun CardedComponent(padding: Dp, composable: @Composable ColumnScope.() -> Unit) {
    Card {
        Column(Modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
            composable()
        }
    }
}