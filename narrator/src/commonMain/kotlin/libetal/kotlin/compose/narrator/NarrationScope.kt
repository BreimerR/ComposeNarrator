package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.coroutines.IoDispatcher
import libetal.multiplatform.log.Log

class NarrationBackStack<Key>(activities: SnapshotStateList<Key>, onEmpty: () -> Boolean) : ListBackStack<Key>(activities, onEmpty)

/**
 * Provides for adding components into the back stack
 * */
class NarrationScope<Key>(
    private val backStack: NarrationBackStack<Key>,
    private val enterTransition: EnterTransition,
    private val exitTransition: ExitTransition
) {

    var isTransitioning: Boolean = false

    private var components = mutableStateMapOf<Key, @Composable () -> Unit>()

    /**
     * Initializes marked composables that
     * can be navigated to in the stack
     **/
    operator fun Key.invoke(composable: @Composable () -> Unit) {
        /**
         * Makes the first item in the stack to be the visible
         **/

        if (backStack.isEmpty) backStack.navigateTo(this)

        components[this] = composable

    }

    var initial = 0

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun narrate() {

        val current = backStack.current

        val component = components[current]

        /**NOT SURE HOW THIS COULD HAPPEN YET**/
        @Suppress("FoldInitializerAndIfToElvis")
        if (component == null) throw RuntimeException("Can't navigate to $current.")


        isTransitioning = true
        /**
         * On launch there is a jerk
         * this avoids that jerk
         * it's most probably being called by state
         * variable being updated twice but not sure which
         **/

        if (initial++ < 2) {
            component()
            isTransitioning = false
        } else AnimatedContent(
            component,
            transitionSpec = {
                enterTransition with exitTransition
            },
        ) {
            it()
            isTransitioning = false
        }

        DisposableEffect(component) {


            onDispose {
                // Start a timer to clear the view model
                // viewModel.pendingDispose()
            }
        }

    }

    /**
     * Don't think I can add a key that's not a
     * member of the enum. Would mean constraining stack keys to enums
     * to avoid adding invalid keys to a stack.
     * */
    internal fun navigateTo(key: Key) =
        if (!isTransitioning) backStack.navigateTo(key) else Log.d("Narration", "Transitioning: Event not consumed")

    internal fun back(onEmpty: () -> Boolean): Boolean = if (!isTransitioning) backStack.back(onEmpty) else true

    companion object {
        val TAG = "NarrationScope"
    }
}
