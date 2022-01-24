package libetal.kotlin.compose.narrator.backstack

open class BackStackException(message: String) : RuntimeException(message)

class EmptyBackStackException : BackStackException("Trying to retrieve a page while backStack hasn't been updated")