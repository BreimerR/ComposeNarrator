package libetal.kotlin.compose.narrator.common

import androidx.compose.animation.*
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
import libetal.kotlin.compose.narrator.common.AppNarrations.*
import libetal.kotlin.compose.narrator.common.data.User
import libetal.kotlin.compose.narrator.common.models.HomeViewModel
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope
import libetal.kotlin.compose.narrator.invoke
import libetal.kotlin.debug.info

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
                    scope.back()
                }) {
                    Icon(Icons.Default.ArrowBack, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
            Row {
                IconButton({
                    // scope.narrate(AppNarrations.SETTINGS)
                    with(scope) {
                        SETTINGS.narrate()
                    }
                }) {
                    Icon(Icons.Default.Settings, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            val userState = mutableStateOf<User?>(null)

            Narration<AppNarrations> {

                AppNarrations.HOME {

                    CardedComponent(4.dp) {
                        Text("Home")
                        Button({
                            SETTINGS.narrate()
                        }) {
                            Text("Settings")
                        }
                        Text("Videos")
                        Button({
                            VIDEOS.narrate()
                        }) {
                            Text("Videos")
                        }
                    }
                }

                AppNarrations.SETTINGS {
                    "App" info "Recreating SETTINGS"
                    Narration(userState, fadeIn() + slideInVertically { it }, slideOutVertically { -it }) {
                        "App" info "Recreating state Narration"
                        val t = this

                        val login = createPremise { it == null }
                        val edit = createPremise { it != null }

                        login {
                            var name by remember { mutableStateOf("") }

                            CardedComponent(4.dp) {
                                TextField(name, {
                                    name = it
                                })

                                Button({
                                    currentValue = User(name)
                                }) {
                                    Text("Save")
                                }
                            }

                            this@login.addOnExitRequest {
                                currentValue != null
                            }

                        }

                        edit {
                            val user = currentValue!!

                            CardedComponent(4.dp) {
                                Row {
                                    Text("Name:")
                                    CardedComponent(2.dp) {
                                        Text(user.name)
                                    }
                                }
                                Button({
                                    currentValue = null
                                }) {
                                    Text("Clear")
                                }
                            }
                        }

                    }

                }

                AppNarrations.VIDEOS({ HomeViewModel() }) {

                    var allowExit by remember { it.allowExitState }
                    val count by remember { it.countState }

                    CardedComponent(4.dp) {
                        Switch(allowExit, {
                            allowExit = it
                        })
                        Text("Count at $count")
                        Button({
                            SETTINGS.narrate()
                        }) {
                            Text("Settings")
                        }
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


