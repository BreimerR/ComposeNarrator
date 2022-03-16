package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.historyScope
import libetal.kotlin.compose.narrator.narration


@Composable
fun App(onAppCloseRequest: () -> Unit) {

    Column {

        Narration({
            onAppCloseRequest()
            true
        }) {

            Test.HelloWorld {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val narration = Test.AnotherOne.narration
                    Button({
                        narration.begin()
                    }) {
                        Text("Next narration")
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Hello World")
                }
            }

            Test.AnotherOne {
                val history = historyScope

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button({
                        history.begin {
                            onAppCloseRequest()
                            true
                        }
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

