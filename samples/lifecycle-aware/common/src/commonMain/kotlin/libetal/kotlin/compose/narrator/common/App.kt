package libetal.kotlin.compose.narrator.common

import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import libetal.kotlin.compose.narrator.common.AppNarrations.SETTINGS
import libetal.kotlin.compose.narrator.common.AppNarrations.VIDEOS
import libetal.kotlin.compose.narrator.common.data.User
import libetal.kotlin.compose.narrator.common.models.HomeViewModel
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Composable
fun App() =
    Column(Modifier.fillMaxSize()) {

        val scope by createScopeCollector<ProgressiveNarrationScope<AppNarrations, ScopedComposable<ProgressiveNarrativeScope>>>()

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

                AppNarrations.SETTINGS(this, { HomeViewModel() }) {
                    // NOTICE: Keeping states here isn't advisable as the behaviour isn't as you'd prefer
                    Narration(it.userState, fadeIn() + slideInVertically { it }, slideOutVertically { -it }) {

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

                            addOnExitRequest {
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

                AppNarrations.VIDEOS(this, { HomeViewModel() }) {

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

                    addOnExitRequest {
                        allowExit
                    }

                }

            }
        }
    }


@Composable
fun CardedComponent(padding: Dp, composable: @Composable ColumnScope.() -> Unit) = Card {
    Column(Modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
        composable()
    }
}