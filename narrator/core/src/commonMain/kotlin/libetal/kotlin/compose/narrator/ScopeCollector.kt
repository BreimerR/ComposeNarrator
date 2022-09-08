package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import kotlin.reflect.KProperty

val scopeCollectors = mutableListOf<ScopeCollector<*>>()

val collectedScope
    get() = ScopeCollector<NarrationScope<out Any, *,*>>().also {
        scopeCollectors.add(it)
    }


fun <T : NarrationScope<out Any, *,*>> createScopeCollector(onScopeCollected: (T.() -> Unit)? = null) =
    ScopeCollector(onScopeCollected).also {
        scopeCollectors.add(it)
    }

class ScopeCollector<Scope : NarrationScope<out Any, *,*>>(private val onScopeCollected: (Scope.() -> Unit)? = null) {

    var scope: Scope? = null
        private set

    operator fun getValue(receiver: Any?, property: KProperty<*>) =
        scope
            ?: throw RuntimeException("No scope initialized in application. Prepare the collector before the NarrationScope you want to collect.")

    infix fun <NScope : NarrationScope<out Any, *,*>> collect(collectedScope: NScope) {
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