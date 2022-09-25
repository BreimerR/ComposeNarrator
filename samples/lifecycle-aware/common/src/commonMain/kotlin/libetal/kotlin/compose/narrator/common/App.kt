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
import libetal.kotlin.compose.narrator.common.models.VideosViewModel
import libetal.kotlin.compose.narrator.interfaces.ProgressiveNarrationScope

@Composable
fun App() =
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

                SETTINGS(this, { HomeViewModel() }) {

                    Narration(remember { it.userState }) {
                        val exists = createPremise { it != null }
                        val missing = createPremise { it == null }

                        exists { user ->
                            Column {
                                Text(user!!.name)
                                Button({
                                    currentValue = null
                                }) {
                                    Text("Delete")
                                }
                            }
                        }

                        missing {

                            Column {

                                val text = remember { mutableStateOf(it?.name ?: "") }

                                TextField(text.value, {
                                    text.value = it
                                })

                                Button({
                                    currentValue = User(text.value)
                                }) {
                                    Text("Save")

                                }
                            }
                        }
                    }
                }

                VIDEOS(this, { VideosViewModel() }) {

                    val allowExitState = remember { it.allowExitState }
                    val count = remember { it.countState }

                    CardedComponent(4.dp) {
                        Switch(allowExitState.value, {
                            allowExitState.value = it
                        })
                        Text("Count at ${count.value}")
                        Button({
                            SETTINGS.narrate()
                        }) {
                            Text("Settings")
                        }
                    }

                    addOnExitRequest {
                        allowExitState.value
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