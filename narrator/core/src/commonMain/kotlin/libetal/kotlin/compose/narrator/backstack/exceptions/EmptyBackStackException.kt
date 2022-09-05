package libetal.kotlin.compose.narrator.backstack.exceptions

class EmptyBackStackException : BackStackException("Trying to retrieve a page while backStack hasn't been updated")