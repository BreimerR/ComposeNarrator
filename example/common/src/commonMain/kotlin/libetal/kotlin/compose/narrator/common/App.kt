package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.StoryScope
import libetal.kotlin.compose.narrator.historyScope
import libetal.kotlin.compose.narrator.lifecycle.LifeCycleAware
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.ViewModelLifeCycleObserver
import libetal.kotlin.compose.narrator.narration


@Composable
fun App(prepareNarrations: StoryScope<*, *>.() -> Unit, onAppCloseRequest: () -> Unit) {

    Column {

        Narration<Test>({
            onAppCloseRequest()
            true
        }) {

            prepareNarrations()

            Test.HelloWorld({ HelloWorldViewModel() }) {
                var state by remember { mutableStateOf(state) }
                val count by remember { countState }
                val persistent by remember { persistentState }
                val message by remember { messageState }

                addObserver(object : ViewModelLifeCycleObserver {
                    override fun onStateChangeListener(owner: LifeCycleAware, newState: Lifecycle.State) {
                        state = newState
                    }
                })

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val anotherNarration = Test.AnotherOne.narration

                    Button({
                        anotherNarration.begin()
                    }) {
                        Text("Another One")
                    }
                    Button({
                        this@HelloWorld.state = Lifecycle.State.CREATED
                    }) {
                        Text("CREATE")
                    }
                    Button({
                        resume()
                    }) {
                        Text("RESUME")
                    }
                    Button({
                        start()
                    }) {
                        Text("START")
                    }
                    Button({
                        pause()
                    }) {
                        Text("PAUSE")
                    }

                    Text("Persisting: $persistent")
                    Text("$message: $count")

                }
            }

            Test.AnotherOne {
                val controlState = mutableStateOf(false)
                Column {
                    Narration(controlState, false) {
                        val showDisplay = { _: Boolean -> true }
                        val onHideDisplay = { newState: Boolean -> newState }

                        showDisplay {
                            Text("Always visible")
                            Button({
                                controlState.value = !controlState.value
                            }) {
                                Text("Change")
                            }
                        }

                        onHideDisplay {
                            Text("Always hides")
                        }

                    }
                }
            }

        }

    }

}


