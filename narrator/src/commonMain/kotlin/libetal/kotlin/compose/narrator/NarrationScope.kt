package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.lifecycle.*
import libetal.kotlin.compose.narrator.lifecycle.LocalViewModelProvider
import libetal.multiplatform.log.Log
import libetal.kotlin.compose.narrator.lifecycle.ViewModel as LifeCycleViewModel

class NarrationBackStack<Key>(activities: SnapshotStateList<Key>) : ListBackStack<Key>(activities)

/**
 * Provides for adding components into the back stack
 * */
class NarrationScope<Key>(
    internal var backStack: NarrationBackStack<Key>,
    private val enterTransition: EnterTransition,
    private val exitTransition: ExitTransition
) : Lifecycle.Owner<LifeCycleRegistry> {

    var isTransitioning: Boolean = false

    private var initial = 0

    /**
     * Not sure if this is needed yet or not
     **/
    override val lifeCycle
        get() = backStack.current.lifecycle

    private val lifeCycles by lazy {
        mutableMapOf<Key, LifeCycleRegistry>()
    }

    val Key.lifecycle: LifeCycleRegistry
        get() {

            val key = this

            var registry = lifeCycles[this]

            if (registry == null) {
                registry = LifeCycleRegistry().also {

                    it.addObserver { event ->

                        val viewModel = key.viewModel<LifeCycleViewModel>()

                        when (event) {

                            Lifecycle.Event.ON_CREATE -> {
                                viewModel?.create()
                                viewModel?.apply {
                                    create()
                                    Log.d(TAG, "Creating ${this::class.qualifiedName}")
                                }

                            }

                            Lifecycle.Event.ON_START -> {
                                viewModel?.start()
                            }

                            Lifecycle.Event.ON_RESUME -> {
                                viewModel?.resume()
                            }

                            Lifecycle.Event.ON_PAUSE -> {
                                Log.d(TAG, "Paused $key")
                            }

                            Lifecycle.Event.ON_ANY -> {
                                Log.d(TAG, "ANY $event ")
                            }

                            Lifecycle.Event.ON_DESTROY -> {
                                Log.d(TAG, "Destroying $key")
                                viewModel?.apply {
                                    pause()
                                    viewModelStore.remove(this)
                                }

                            }

                        }
                    }

                    lifeCycles[this] = it
                }
            }

            return registry
        }

    internal val viewModelStore by lazy {
        ViewModelStore<Key>()
    }

    private val components by lazy {
        mutableStateMapOf<Key, @Composable () -> Unit>()
    }

    /**
     * Initializes marked composables that
     * can be navigated to in the stack
     **/
    operator fun Key.invoke(content: @Composable Key.() -> Unit) {
        add {
            content(this)
        }
    }

    operator fun <VM : LifeCycleViewModel> Key.invoke(viewModelProvider: () -> VM, content: @Composable Key.() -> Unit) {

        viewModelStore[this] = viewModelProvider

        add {
            content(this)
        }

    }

    @Suppress("UNCHECKED_CAST")
    fun <VM : LifeCycleViewModel> Key.lifeCycleViewModel(): VM = try {
        viewModel()!!
    } catch (e: NullPointerException) {
        throw RuntimeException(
            """|
                | Check NarrationScope<Key>
                | and make sure to use 
                | operator fun Key.invoke(viewModelFactory:()->ViewModel) instead of
                | operator fun Key.invoke() instead of
            """.trimMargin()
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM : LifeCycleViewModel?> Key.viewModel() = viewModelStore[this] as? VM

    private fun Key.add(content: @Composable () -> Unit) {
        /**
         * Makes the first item in the stack to be the visible
         **/
        if (backStack.isEmpty) backStack.navigateTo(this)
        /*Prevents having same key twice*/
        components[this] = content

    }

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
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun narrate() {

        val current = backStack.current

        if (!current.lifecycle.wasCreated) current.lifecycle.create()

        val viewModel = viewModelStore.find(current)

        val component = components[current]

        /**NOT SURE HOW THIS COULD HAPPEN YET**/
        @Suppress("FoldInitializerAndIfToElvis")
        if (component == null) throw RuntimeException("Can't navigate to $current.")

        CompositionLocalProvider(LocalViewModelProvider provides viewModel) {
            /** TODO
             * On launch there is a jerk
             * this avoids that jerk
             * it's most probably being called by state
             * variable being updated twice but not sure which
             **/
            if (initial++ < 2) {
                component()
            } else AnimatedContent(
                component,
                transitionSpec = {
                    enterTransition with exitTransition
                },
                contentAlignment = Alignment.CenterEnd
            ) {
                isTransitioning = this.transition.currentState != this.transition.targetState

                if (!isTransitioning) current.lifecycle.start()

                it()
            }
        }

        LaunchedEffect(current) {
            // TODO this can be used here this.coroutineContext.job
            current.lifecycle.resume()
        }

        DisposableEffect(current) {
            onDispose {
                current.lifecycle.pause()
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

    internal fun back(onEmpty: (() -> Boolean)? = null): Boolean = if (!isTransitioning) backStack.back(onEmpty) else true

    companion object {
        private const val TAG = "NarrationScope"
    }

}
