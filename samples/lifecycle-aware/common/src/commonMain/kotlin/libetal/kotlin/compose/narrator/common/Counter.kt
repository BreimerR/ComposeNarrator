package libetal.kotlin.compose.narrator.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.NarrationJvm
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack


@Composable
fun Counter() = Narration<CounterNarrations,CounterViewModel>(
    { uuid, stack ->
        NarrationScopeImpl<CounterNarrations>(
            uuid,
            NarrationBackStack(
                stack
            )
        )
    }, { CounterViewModel() }) { counterViewModel ->

    CounterNarrations.HOME {
        val header = remember { counterViewModel.headerCounters }
        val footerLeft = remember { counterViewModel.footerLeft }
        val footerRight = remember { counterViewModel.footerRight }

        val hI = remember { counterViewModel.hI }
        val fLi = remember { counterViewModel.fLi }
        val fRi = remember { counterViewModel.fRi }

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

            CardedComponent(8.dp) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button({
                        counterViewModel.addHeaderCounter()
                    }) {
                        Text("Next Child")
                    }
                    Spacer(Modifier.height(6.dp))
                    Button({
                        counterViewModel.removePrevHeaderCounter()
                    }) {
                        Text("Prev Child")
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("Some Text ${header.getOrNull(hI.value) ?: 0}")
                }

                Row(horizontalArrangement = Arrangement.SpaceAround) {
                    Column {
                        Button({

                        }) {
                            Text("Next Child")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button({

                        }) {
                            Text("Prev Child")
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Button({

                        }) {
                            Text("Next Child")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button({

                        }) {
                            Text("Prev Child")
                        }
                    }
                }

            }
        }
    }
}


enum class CounterNarrations {
    HOME,
    RESET;
}