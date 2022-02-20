package libetal.kotlin.compose.narrator

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import libetal.kotlin.compose.narrator.backstack.BackStack
import libetal.kotlin.compose.narrator.chapters.Chapter
import libetal.kotlin.compose.narrator.coroutines.IoDispatcher
import libetal.kotlin.compose.narrator.view_models.ViewModel

open class Narration<Index : Any>(
    currentPage: Index,
    val enterTransition: EnterTransition = slideInVertically { height -> height } + fadeIn(),
    val exitTransition: ExitTransition = slideOutVertically { height -> -height } + fadeOut(),
    val onNarrationEnd: () -> Unit = { throw RuntimeException("Unhandled narration end. No open chapters existing") }
) {
    private lateinit var indexes: SnapshotStateList<Index>

    private val chapters: MutableMap<Index, Chapter<in ViewModel>> = mutableMapOf()

    private val targetContentState = mutableStateOf<ViewModel?>(null)

    internal val backStack by lazy {
        BackStack(mutableStateListOf(currentPage))
    }

    val lifeCycleScope by lazy {
        CoroutineScope(SupervisorJob() + IoDispatcher)
    }

    val onResumeData by lazy {
        mutableMapOf<Index, Any?>()
    }

    val currentChapter
        get() = chapters[backStack.active] ?: throw RuntimeException("Invalid page set")


    operator fun plusAssign(pair: Pair<Index, Chapter<in ViewModel>>) {
        chapters += pair
    }

    operator fun get(title: Index): Chapter<*> = chapters[title] ?: throw RuntimeException("Undefined Chapter $title")


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    internal fun begin() {

        val targetContent by remember { targetContentState }

        currentChapter.contentFactory?.create(backStack.active, targetContentState)

        AnimatedContent(targetState = currentChapter, transitionSpec = {
            targetState.enterTransition with initialState.exitTransition
        }) { chapter ->
            chapter.content(targetContent)
        }

    }
}

@Deprecated("Naming convention for a storybook doesn't conform")
fun <Index : Any, D> Narration<Index>.fastForwardTo(page: Index, data: D) {
    lifeCycleScope.launch {
        backStack.navigateTo(page)

        currentChapter.contentFactory?.onCreateData?.set(page as Any, mutableStateOf(data))

        currentChapter.contentFactory?.updateViewModelData(data)

    }
}

fun <Index : Any, D> Narration<Index>.turnTo(newPage: Index, data: D) {
    lifeCycleScope.launch {

        backStack.navigateTo(newPage)

        currentChapter.contentFactory?.onCreateData?.set(newPage, mutableStateOf(data))
        currentChapter.contentFactory?.updateViewModelData(data)

    }
}

infix fun <Index : Any> Narration<Index>.turnTo(newPage: Index) = turnTo<Index, Any?>(newPage, null)

/**
 * A unified messaging system is required to
 * avoid having to type cast the data everytime
 * Kotlin serialize is nice
 * but looking to implement something similar
 * to protocol buffers
 * */
fun <Index : Any, D> Narration<Index>.previousChapter(data: D? = null, onNarrationsEnd: () -> Unit = { onNarrationEnd() }) =
    backStack.previous(onNarrationsEnd) {
        onResumeData[backStack.active] = data
    }

fun <Index : Any> Narration<Index>.previousChapter(onNarrationsEnd: () -> Unit = this.onNarrationEnd) =
    previousChapter<Index, Any>(null, onNarrationsEnd)


