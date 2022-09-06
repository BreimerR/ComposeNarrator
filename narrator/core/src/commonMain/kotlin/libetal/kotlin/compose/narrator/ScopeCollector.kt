package libetal.kotlin.compose.narrator

import libetal.kotlin.compose.narrator.interfaces.NarrationScope
import kotlin.reflect.KProperty

val scopeCollectors = mutableListOf<ScopeCollector>()

val collectedScope
    get() = ScopeCollector().also {
        scopeCollectors.add(it)
    }

class ScopeCollector {

    var scope: NarrationScope<out Any, *>? = null
        private set

    operator fun getValue(receiver: Any?, property: KProperty<*>) =
        scope
            ?: throw RuntimeException("No scope initialized in application. Prepare the collector before the NarrationScope you want to collect.")

    infix fun <Key : Any, C> collect(scope: NarrationScope<Key, C>) {
        this.scope = scope
    }

}