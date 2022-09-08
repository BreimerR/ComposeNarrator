package libetal.kotlin.compose.narrator.lifecycle


interface LifeCycleAware<L : Lifecycle> : Lifecycle.Owner<L>, Lifecycle.Callbacks {

    companion object {
        const val TAG = "LifeCycleAware"
    }

}





