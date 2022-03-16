package libetal.kotlin.compose.narrator

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import kotlin.reflect.KProperty

class CompositionProvider<T>(initialValue: T? = null) {
    var provider: ProvidableCompositionLocal<T>? = initialValue.compositionLocalOf()

    operator fun getValue(receiver: Nothing?, kProperty: KProperty<*>): ProvidableCompositionLocal<T> {
        if (provider == null)
            provider = compositionLocalOf { noLocalProvidedFor(kProperty.name) }

        return provider!!
    }

    operator fun <R> getValue(receiver: R?, kProperty: KProperty<*>): ProvidableCompositionLocal<T> {
        if (provider == null)
            provider = compositionLocalOf { noLocalProvidedFor(kProperty.name) }

        return provider!!
    }

    private fun T?.compositionLocalOf(): ProvidableCompositionLocal<T>? = this?.let { compositionLocalOf { it } }
}
