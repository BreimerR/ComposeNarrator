package libetal.kotlin.compose.narrator


import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.snapshots.StateObject
import androidx.lifecycle.LifecycleOwner
import kotlinx.datetime.Clock
import libetal.kotlin.compose.narrator.lifecycle.Lifecycle
import java.util.*

interface View {
    val uuid: String

    @Composable
    operator fun invoke()

    companion object : ViewComposer {

        override fun invoke() = throw RuntimeException("Implement your own")

    }

}

interface ViewComposer {
    operator fun invoke(): View
}

interface Composer {
    @Composable
    operator fun invoke()
}

interface ViewGroup : View {

    val children: SnapshotStateMap<String, View>

    @Composable
    override fun invoke() {

    }

}

interface BackStackOwner : ViewGroup {

    var entryAnimation: Any

    var exitAnimation: Any

    fun addToBackStack(view: View) = children.set(view.uuid, view)

    fun addToBackStack(content: @Composable () -> Unit) {

        val view = object : View {

            override val uuid: String by lazy {
                Clock.System.now().epochSeconds.toString()
            }

            @Composable
            override fun invoke() {
                content()
            }

        }

        children[view.uuid] = view

    }


}

interface BackStackRouter : BackStackOwner {
    var currentView: View

}

interface Window : BackStackRouter, LifecycleOwner

interface Fragment : BackStackRouter, LifecycleOwner {

}

interface Application : BackStackRouter, LifecycleOwner {

}


