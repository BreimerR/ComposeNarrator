package libetal.kotlin.compose.narrator

import androidx.compose.runtime.*
import libetal.kotlin.compose.narrator.backstack.NarrationBackStack

/**
 * @param backHandler
 * If provided handles
 **/
@Composable
fun <Key : Any> Narration(prepareComponents: NarrationScopeImpl<Key>.() -> Unit) =
    remember { mutableStateListOf<Key>() }.let { stack ->
        Narration(
            { uuid ->
                NarrationScopeImpl(
                    uuid,
                    NarrationBackStack(
                        stack
                    )
                )
            },
            prepareComponents
        )
    }