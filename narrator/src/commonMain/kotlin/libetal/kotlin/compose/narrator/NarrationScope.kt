package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.lifecycle.LocalViewModelProvider
import libetal.kotlin.compose.narrator.lifecycle.StoryViewModelStore
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.debug.debug
import libetal.multiplatform.log.Log


/**
 * Provides for adding components into the back stack
 * */
class NarrationScope<Key>(
    backStack: NarrationBackStack<Key>,
    private val enterTransition: EnterTransition,
    private val exitTransition: ExitTransition
) : StoryScope<Key, NarrationBackStack<Key>>(backStack) {

    override val shouldExit: Boolean
        get() = backStack.isAlmostEmpty

    var isTransitioning: Boolean = false

    /** TODO
     * Allow narration nesting
     * This is already supported
     * but I was thinking in terms maybe of
     * dialogs being children of the narration and
     * on back pressed that child is killed and
     * the current narration resumes
     * also prevent's having the current viewModel from being
     * destroyed. Behaviour not analysed but might be happening
     **/
    @Composable
    @OptIn(ExperimentalAnimationApi::class)
    override fun narrate() {

        val current = backStack.current

        val component = components[current]

        /**
         * This could happen in situations of
         * unregistered components
         * Not sure how to handle them yet
         * but having IDE support would be nice
         **/
        @Suppress("FoldInitializerAndIfToElvis")
        if (component == null) throw RuntimeException("Can't navigate to $current. Direct to documentation...")

        /**
         * TODO
         * this might be irrelevant as keys provide the
         * required viewModel in invoke
         **/
        AnimatedContent(
            component,
            transitionSpec = {
                enterTransition with exitTransition
            },
            contentAlignment = Alignment.CenterEnd
        ) {
            isTransitioning = this.transition.currentState != this.transition.targetState

            // if (!isTransitioning) current.lifecycle.start()

            it()

        }

        /*LaunchedEffect(current) {
            current.lifecycle.resume()
        }

        DisposableEffect(current) {
            onDispose {
                current.lifecycle.pause()
            }
        }*/

    }

    /**
     * Initializes marked composables that
     * can be navigated to in the stack
     **/
    operator fun Key.invoke(content: @Composable Key.() -> Unit) = add {
        content(this)
    }

     operator fun <VM : ViewModel> Key.invoke(viewModelProvider: () -> VM, content: @Composable VM.(Key) -> Unit) {

        StoryViewModelStore[storeKey] = viewModelProvider

        add {

            val viewModel = lifeCycleViewModel<VM>()

            if (!viewModel.wasCreated)
                viewModel.create()

            CompositionLocalProvider(LocalViewModelProvider provides viewModel) {
                content(viewModel, this)
            }

            LaunchedEffect(viewModel) {
                viewModel.resume()
            }

            DisposableEffect(viewModel) {
                onDispose {
                    viewModel.pause()
                }
            }

        }

    }


    /**
     * Don't think I can add a key that's not a
     * member of the enum. Would mean constraining stack keys to enums
     * to avoid adding invalid keys to a stack.
     * */
    fun navigateTo(key: Key) =
        if (!isTransitioning) backStack.navigateTo(key) else Log.d("Narration", "Transitioning: Event not consumed")

    override fun back(): Boolean = if (!isTransitioning) {
        var exitState = true

        for ((_, child) in children) {
            exitState = exitState && child.back()
        }

        if (exitState) backStack.back()
        else false
    } else false

    companion object {
        private const val TAG = "NarrationScope"
    }

}
