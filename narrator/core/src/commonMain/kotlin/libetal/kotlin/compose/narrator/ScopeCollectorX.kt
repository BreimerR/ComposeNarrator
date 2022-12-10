package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope

val scopeCollectors = mutableListOf<ScopeCollector<*>>()

val collectedScope
    get() = ScopeCollector<NarrationScope<out Any, out NarrativeScope, *>>().also {
        scopeCollectors.add(it)
    }


fun <T : NarrationScope<out Any, out NarrativeScope, *>> createScopeCollector(onScopeCollected: (T.() -> Unit)? = null) =
    ScopeCollector(onScopeCollected).also {
        scopeCollectors.add(it)
    }
