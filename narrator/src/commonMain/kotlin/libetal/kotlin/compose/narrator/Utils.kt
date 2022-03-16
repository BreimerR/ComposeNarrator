package libetal.kotlin.compose.narrator

fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

fun <T> compositionProvider(initialValue: T? = null): CompositionProvider<T> = CompositionProvider(initialValue)