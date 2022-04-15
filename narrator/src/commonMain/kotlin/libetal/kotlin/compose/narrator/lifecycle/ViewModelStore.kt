package libetal.kotlin.compose.narrator.lifecycle

open class ViewModelStore<Key> {

    internal val store by lazy {
        mutableMapOf<Key, ViewModel>()
    }

    private val initializers by lazy {
        mutableMapOf<Key, () -> ViewModel>()
    }

    operator fun get(key: Key) = store[key] ?: key.createViewModel()

    private fun Key.createViewModel() = initializers[this]?.invoke()?.also { viewModel ->

        viewModel.addObserver(object : ViewModelLifeCycleObserver {
            override fun onStateChangeListener(owner: LifeCycleAware, state: Lifecycle.State) {
                when (owner.state) {
                    Lifecycle.State.DESTROYED -> this@ViewModelStore.remove(viewModel)
                    else -> {

                    }
                }
            }
        })

        store[this] = viewModel

    }

    operator fun set(key: Key, factory: () -> ViewModel) {
        if (key.hasInitializer) return
        initializers[key] = factory
    }

    fun find(key: Key): ViewModel? = store[key]

    internal fun remove(viewModel: ViewModel) {
        viewModel.key?.also {
            store.remove(it)
        }
    }

    private val ViewModel.key: Key?
        get() {
            var result: Key? = null

            for ((key, viewModel) in store) {
                if (viewModel == this) result = key
            }

            return result
        }

    private val Key.hasInitializer
        get() = this in initializers
}