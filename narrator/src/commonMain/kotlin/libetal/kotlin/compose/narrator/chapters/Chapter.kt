package libetal.kotlin.compose.narrator.chapters

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.view_models.ViewModel
import libetal.kotlin.compose.narrator.view_models.ViewModelFactory


data class Chapter<VM : ViewModel>(
    val enterTransition: EnterTransition,
    val exitTransition: ExitTransition,
    val contentFactory: ViewModelFactory<VM>? = null,
    val content: @Composable VM?.() -> Unit
)

fun <T: Any> Narration<T>.chapter(
    page: T,
    enterTransition: EnterTransition = this@chapter.enterTransition,
    exitTransition: ExitTransition = this@chapter.exitTransition,
    viewModelFactory: ViewModelFactory<in ViewModel>? = null,
    content: @Composable ViewModel?.() -> Unit
) {
    this += page to Chapter(enterTransition, exitTransition, viewModelFactory, content)
}

fun <T: Any, VM : ViewModel> Narration<T>.chapter(
    page: T,
    enterTransition: EnterTransition = this@chapter.enterTransition,
    exitTransition: ExitTransition = this@chapter.exitTransition,
    viewModelFactory: suspend () -> VM,
    content: @Composable VM?.() -> Unit
) {

    @Suppress("UNCHECKED_CAST")
    this += page to Chapter(
        enterTransition, exitTransition,
        object : ViewModelFactory<VM>() {
            override suspend fun createViewModel(data: Any?): VM =  viewModelFactory()
        } as ViewModelFactory<in ViewModel>
    ) {
        content(this as? VM)
    }

}


