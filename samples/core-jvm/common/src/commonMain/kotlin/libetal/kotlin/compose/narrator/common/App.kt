package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.ComposableFun
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Composable
fun App() =
    Column(Modifier.fillMaxSize()) {

        val scope by createScopeCollector<ProgressiveNarrationScope<AppNarrations, ComposableFun>>()

        Row(
            Modifier.fillMaxWidth().height(56.dp).background(MaterialTheme.colors.primary)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton({
                    scope.back {
                        for (listener in scope.onNarrationEndListeners) {
                            listener()
                        }
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
            Row {
                IconButton({
                    // scope.narrate(AppNarrations.SETTINGS)
                    with(scope) {
                        AppNarrations.SETTINGS.narrate()
                    }
                }) {
                    Icon(Icons.Default.Settings, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Narration<AppNarrations> {
                AppNarrations.HOME {

                    CardedComponent(4.dp) {
                        Text("Home")
                        Button({
                            AppNarrations.SETTINGS.narrate()
                        }) {
                            Text("Settings")
                        }
                        Text("Videos")
                        Button({
                            AppNarrations.VIDEOS.narrate()
                        }) {
                            Text("Videos")
                        }
                    }
                }



                AppNarrations.SETTINGS {
                    val userState = remember { mutableStateOf<User?>(null) }
                    Narration(userState) {
                        val login = { user: User? -> user == null }
                        val edit = { user: User? -> user != null }

                        login {
                            var name by remember { mutableStateOf("") }
                            CardedComponent(4.dp) {
                                TextField(name, {
                                    name = it
                                })

                                Button({
                                    userState.value = User(name)
                                }) {
                                    Text("Save")
                                }
                            }
                        }

                        edit {
                            CardedComponent(4.dp) {
                                Row {
                                    Text("Name:")
                                    CardedComponent(2.dp) {
                                        Text(userState.value?.name ?: "")
                                    }
                                }
                            }
                        }
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


data class User(val name: String)