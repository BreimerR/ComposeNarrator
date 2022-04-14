package libetal.kotlin.compose.narrator

interface NarrationController<Key> {
    val scope: StoryScope<Key,*>
}

interface NarrationDestination<Key> : NarrationController<Key> {
    fun begin()
}

interface NarrationHistory : NarrationController<Any> {
    fun begin(): Boolean
}


