package libetal.kotlin.compose.narrator.interfaces

interface NavigationControllerKey<T> {
    val targets: Array<out T>
}