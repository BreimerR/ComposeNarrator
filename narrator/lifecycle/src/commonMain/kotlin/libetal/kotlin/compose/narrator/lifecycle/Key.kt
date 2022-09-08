package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.name

/**
 * A unique tag for any key provided
 * in a project.
 * In a situation where
 * the keys unique states are compromised
 * use of Enums might be enforced but current'y
 * cases that are supported seem to have  unique esence
 * without much effort
 **/
val <Key> Key.storeKey
    get() = when (this) {
        is String -> this
        is Enum<*> -> this.name
        is Function<*> -> "kotlin.jvm.functions.Function:${this.hashCode()}"
        else -> "${this?.let { it::class.name } ?: ""}.${this.hashCode()}"
    }