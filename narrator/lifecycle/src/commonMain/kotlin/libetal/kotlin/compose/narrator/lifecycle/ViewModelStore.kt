package libetal.kotlin.compose.narrator.lifecycle

import libetal.kotlin.laziest

open class ViewModelStore<Key> {

    /**
     * TODO: This model won't work on NATIVE platforms best use ThreadLocal
     **/
    internal val store by laziest {
        mutableMapOf<Key, ViewModel>()
    }

    private val initializers by laziest {
        mutableMapOf<Key, () -> ViewModel>()
    }

    operator fun get(key: Key) = store[key] ?: key.createViewModel() ?: throw RuntimeException(
        """| Make sure to use 
           | fun Key.invoke(viewModelFactory:()->ViewModel, ...)
        """.trimMargin()
    )

    private fun Key.createViewModel() = initializers[this]?.invoke()?.also { viewModel ->
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