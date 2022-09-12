package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import kotlin.reflect.KProperty

val scopeCollectors = mutableListOf<ScopeCollector<*>>()

val collectedScope
    get() = ScopeCollector<NarrationScope<out Any, out NarrativeScope, *>>().also {
        scopeCollectors.add(it)
    }


fun <T : NarrationScope<out Any, out NarrativeScope, *>> createScopeCollector(onScopeCollected: (T.() -> Unit)? = null) =
    ScopeCollector(onScopeCollected).also {
        scopeCollectors.add(it)
    }

class ScopeCollector<Scope : NarrationScope<out Any, out NarrativeScope, *>>(private val onScopeCollected: (Scope.() -> Unit)? = null) {

    var scope: Scope? = null
        get() = field
            ?: throw RuntimeException("No scope initialized in application. Prepare the collector before the NarrationScope you want to collect.")
        private set

    operator fun getValue(receiver: Any?, property: KProperty<*>) =
        scope

    infix fun <NScope : NarrationScope<out Any, out NarrativeScope, *>> collect(collectedScope: NScope) {
        try {
            @Suppress("UNCHECKED_CAST")
            collectedScope as Scope

            scope = collectedScope
            onScopeCollected?.invoke(collectedScope)

        } catch (e: ClassCastException) {
            throw RuntimeException("You collecting wrong scope. Please set collectors before the narration you need a scope from")
        }
    }

}