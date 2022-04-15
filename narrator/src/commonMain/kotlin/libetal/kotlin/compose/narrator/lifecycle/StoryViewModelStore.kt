package libetal.kotlin.compose.narrator.lifecycle

object StoryViewModelStore : ViewModelStore<String>() {
    fun invalidate(key: String, onInvalidate: () -> Unit) {
        if (key in store) {
            store.remove(key)
            onInvalidate()
        }

    }

}