package libetal.kotlin.compose.narrator.interfaces

interface NavigationStateController<T, NK : NavigationControllerKey<T>> {
    /**
     * NOTE
     * This style assumes that the states
     * are organized in some order.
     * And if intercepting states exist the
     * first to return true will be displayed.
     *
     * A way to pass data through the link can
     * be set up through
     **/
    fun NK.display(state: T): Boolean

}