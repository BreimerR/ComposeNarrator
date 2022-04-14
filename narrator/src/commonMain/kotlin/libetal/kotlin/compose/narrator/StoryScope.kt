package libetal.kotlin.compose.narrator

import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import libetal.kotlin.compose.narrator.backstack.ListBackStack
import libetal.kotlin.compose.narrator.lifecycle.*
import libetal.kotlin.debug.info

abstract class StoryScope<Key, BackStack : ListBackStack<Key>>(internal var backStack: BackStack) : LifeCycleAware() {

    protected val components by lazy {
        mutableStateMapOf<Key, @Composable () -> Unit>()
    }

    /**
     * A unique tag for any key provided
     * in a project.
     * In a situation where
     * the keys unique states are compromised
     * use of Enums might be enforced but current'y
     * cases that are supported seem to have  unique esence
     * without much effort
     **/
    val Key.storeKey
        get() = when (this) {
            is Function<*> -> "kotlin.jvm.functions.Function.${this.hashCode()}"
            else -> "${this!!::class.qualifiedName!!}.${this.hashCode()}"
        }

    private val lifeCycles by lazy {
        mutableMapOf<Key, LifeCycleRegistry>()
    }

    protected val Key.lifecycle: LifeCycleRegistry
        get() {

            val key = this

            var registry = lifeCycles[key]

            if (registry == null) {

                registry = LifeCycleRegistry().also {

                    it.addObserver { event ->

                        when (event) {

                            Lifecycle.Event.ON_CREATE -> {

                            }

                            Lifecycle.Event.ON_START -> {

                            }

                            Lifecycle.Event.ON_RESUME -> {

                            }

                            Lifecycle.Event.ON_PAUSE -> {

                            }

                            Lifecycle.Event.ON_ANY -> {

                            }

                            Lifecycle.Event.ON_DESTROY -> {

                            }

                        }
                    }

                    lifeCycles[key] = it

                }
            }

            return registry
        }

    @Suppress("UNCHECKED_CAST")
    fun <VM : ViewModel?> Key.viewModel() = StoryViewModelStore[storeKey] as? VM

    @Composable
    abstract fun narrate()

    private fun Key.add(content: @Composable () -> Unit) {
        backStack.add(this)
        components[this] = content
    }

    abstract fun back(): Boolean

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

    @Suppress("UNCHECKED_CAST")
    fun <VM : ViewModel> Key.lifeCycleViewModel(): VM = try {
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

    override fun addObserver(observer: Observer): Boolean {
        TAG info "Implement:onStateChange"
        return true
    }

    override fun removeObserver(observer: Observer): Boolean {
        LifeCycleAware.TAG info "Implement:onStateChange"
        return true
    }

    companion object {
        private const val TAG = "StoryScope"
    }

}