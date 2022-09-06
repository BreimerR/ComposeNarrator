package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.ComposableFun
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import libetal.kotlin.compose.narrator.narrative

@Composable
fun App(scopeProvider: (NarrationScope<AppNarrations, ComposableFun>) -> Unit) =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Narration<AppNarrations> {
            scopeProvider(this)

            AppNarrations.HOME {

                CardedComponent(4.dp) {
                    Text("Home")
                    Button({
                        AppNarrations.SETTINGS.narrate()
                    }) {
                        Text("Settings")
                    }
                }
            }

            AppNarrations.SETTINGS {

                val videosNarrative = AppNarrations.VIDEOS.narrative

                CardedComponent(4.dp) {
                    Text("Settings View")
                    Button({
                        videosNarrative.narrate()
                    }) {
                        Text("Videos")
                    }
                }

                addOnExitRequest {
                    true
                }

            }

            AppNarrations.VIDEOS {

                val allowExit = remember { mutableStateOf(false) }

                CardedComponent(4.dp) {
                    Text("Videos")
                    Switch(allowExit.value, {
                        allowExit.value = it
                    })
                    Button({
                        AppNarrations.SETTINGS.narrate()
                    }) {
                        Text("Settings")
                    }
                }

                addOnExitRequest {
                    allowExit.value
                }

            }


            /**
             * TODO: Use this way to get narrates to work
             * this are like plot twists or sub stories
             * AppNarrations.VIDEOS_SUBSECTIONS twists {
             *     VideosSubSections()
             * }
             * // bad word given it's can be used in may apps
             * AppNarrations.VIDEOS_SUBSECTIONS setting {
             *     VideosSubSections()
             * }
             **/

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

@Composable
fun VideosSubSections() {

    /*AppNarrations.VIDEOS_HOME narrates {
        Text("Videos Home")
    }

    AppNarrations.VIDEOS_SETTINGS narrates {
        Text("Videos Settings")
    }*/

}