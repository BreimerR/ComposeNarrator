package libetal.kotlin.compose.narrator.common

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutHorizontally
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
import libetal.kotlin.compose.narrator.*
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Composable
fun App() = MaterialTheme {

    Column(Modifier.fillMaxSize()) {

        val scope =
            createScopeCollector<ProgressiveNarrationScope<AppNarrations, ScopedComposable<ProgressiveNarrativeScope>>>()

        Row(
            Modifier.fillMaxWidth().height(56.dp).background(MaterialTheme.colors.primary)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                IconButton({
                    scope.value.back()
                }) {
                    Icon(Icons.Default.ArrowBack, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
            Row {
                IconButton({
                    // scope.narrate(AppNarrations.SETTINGS)
                    with(scope.value) {
                        AppNarrations.SETTINGS.narrate()
                    }
                }) {
                    Icon(Icons.Default.Settings, "BackAction", tint = MaterialTheme.colors.onPrimary)
                }
            }
        }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NarrationJvm {
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

                    NarrationJvm(userState, fadeIn(), slideOutHorizontally { width -> -width }) {
                        val login = createPremise { it == null }
                        val edit = createPremise { it != null }

                        login {
                            val nameState = remember { mutableStateOf("") }
                            CardedComponent(4.dp) {
                                TextField(nameState.value, {
                                    nameState.value = it
                                })

                                Button({
                                    userState.value = User(nameState.value)
                                }) {
                                    Text("Save")
                                }
                            }
                        }

                        edit {
                            val user = it!!

                            CardedComponent(4.dp) {
                                Row {
                                    Text("Name:")
                                    CardedComponent(2.dp) {
                                        Text(user.name)
                                    }
                                }
                                Button({
                                    userState.value = null
                                }) {
                                    Text("Clear")
                                }
                            }
                        }

                        addOnExitRequest {
                            userState.value != null
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


}


data class User(val name: String)
