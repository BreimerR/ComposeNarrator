package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import libetal.kotlin.compose.narrator.*
import libetal.kotlin.compose.narrator.chapters.chapter
import libetal.kotlin.compose.narrator.view_models.ViewModel

@Composable
fun App(onAppCloseRequest: () -> Unit) {

    Column {
        lateinit var narrator: Narration<String>

        Narrator("settings", onNarrationEnd = onAppCloseRequest) {
            narrator = this
            chapter("settings") {
                Text("Hello World")
            }

            chapter("anotherOne", viewModelFactory = { AnotherOneViewModel() }) {
                this?.let {
                    val content = remember { it.onCreateData }
                    Text("Goodbye ${it.onCreateData.value}")
                }
            }

        }

        Row {

            Button(
                {
                    narrator.turnTo("anotherOne", "Breimer")
                },
            ) {
                Text("Breimer Page")
            }

            Button(
                {
                    narrator.turnTo("anotherOne", "Breimer 2")
                },
            ) {
                Text("Breimer 2 page")
            }

            Button({
                narrator.previousChapter("No Data")
            }) {
                Text("Previous page")
            }

        }
    }

}


class AnotherOneViewModel : ViewModel() {

    var name: MutableState<String?> = mutableStateOf("")

    override fun onCreate(data: MutableState<Any?>) {
        super.onCreate(data)
        @Suppress("UNCHECKED_CAST")
        name = data as MutableState<String?>
    }

}

