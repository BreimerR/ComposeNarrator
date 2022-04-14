package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.historyScope
import libetal.kotlin.compose.narrator.lifecycle.LifeCycleAware
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import libetal.kotlin.compose.narrator.lifecycle.ViewModelLifeCycleObserver


@Composable
fun App(onAppCloseRequest: () -> Unit) {

    Column {

        Narration<Test>({
            onAppCloseRequest()
            true
        }) {

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
                val history = historyScope

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button({
                        history.begin()
                    }) {
                        Text("Hello You")
                    }
                    Spacer(Modifier.width(12.dp))

                    Text("Goodbye Good Bye")
                }
            }

        }

    }

}


