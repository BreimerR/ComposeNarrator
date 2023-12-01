package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.interfaces.NarrationScope

class JvmNarrationScope<Key : Any, Scope : NarrativeScope, Content>(
    private val enterTransition: EnterTransition?,
    private val exitTransition: ExitTransition?,
    val delegate: NarrationScope<Key, Scope, Content>
) : NarrationScope<Key, Scope, Content>(
    delegate.uuid,
    delegate.newNarrativeScope
){

    @Composable
    override fun Narrate()  = delegate.Narrate()

}
