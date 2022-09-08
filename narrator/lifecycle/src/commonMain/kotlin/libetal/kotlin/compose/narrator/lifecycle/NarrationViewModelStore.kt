package libetal.kotlin.compose.narrator.lifecycle

object NarrationViewModelStore : ViewModelStore<String>() {
    fun invalidate(key: String, onInvalidate: () -> Unit) {
        if (key in store) {
            store.remove(key)
            onInvalidate()
        }
    }
}
