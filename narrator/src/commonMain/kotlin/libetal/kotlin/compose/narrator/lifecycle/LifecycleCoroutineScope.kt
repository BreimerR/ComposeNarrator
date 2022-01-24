/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libetal.kotlin.compose.narrator.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * [CoroutineScope] tied to a [Lifecycle] and
 * [Dispatchers.Main.immediate][kotlinx.coroutines.MainCoroutineDispatcher.immediate]
 *
 * This scope will be cancelled when the [Lifecycle] is destroyed.
 *
 * This scope provides specialised versions of `launch`: [launchWhenCreated], [launchWhenStarted],
 * [launchWhenResumed]
 */
public abstract class LifecycleCoroutineScope internal constructor() : CoroutineScope {

    internal abstract val lifecycle: Lifecycle

    /**
     * Launches and runs the given block when the [Lifecycle] controlling this
     * [LifecycleCoroutineScope] is at least in [Lifecycle.State.CREATED] state.
     *
     * The returned [Job] will be cancelled when the [Lifecycle] is destroyed.
     *
     * Caution: This API is not recommended to use as it can lead to wasted resources in some
     * cases. Please, use the [Lifecycle.repeatOnLifecycle] API instead. This API will be removed
     * in a future release.
     *
     * @see Lifecycle.whenCreated
     * @see Lifecycle.coroutineScope
     */
    /*public fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job = launch {
        lifecycle.whenCreated(block)
    }*/

    /**
     * Launches and runs the given block when the [Lifecycle] controlling this
     * [LifecycleCoroutineScope] is at least in [Lifecycle.State.STARTED] state.
     *
     * The returned [Job] will be cancelled when the [Lifecycle] is destroyed.
     *
     * Caution: This API is not recommended to use as it can lead to wasted resources in some
     * cases. Please, use the [Lifecycle.repeatOnLifecycle] API instead. This API will be removed
     * in a future release.
     *
     * @see Lifecycle.whenStarted
     * @see Lifecycle.coroutineScope
     */

  /*  public fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job = launch {
        lifecycle.whenStarted(block)
    }
*/
    /**
     * Launches and runs the given block when the [Lifecycle] controlling this
     * [LifecycleCoroutineScope] is at least in [Lifecycle.State.RESUMED] state.
     *
     * The returned [Job] will be cancelled when the [Lifecycle] is destroyed.
     *
     * Caution: This API is not recommended to use as it can lead to wasted resources in some
     * cases. Please, use the [Lifecycle.repeatOnLifecycle] API instead. This API will be removed
     * in a future release.
     *
     * @see Lifecycle.whenResumed
     * @see Lifecycle.coroutineScope
     */
   /* public fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job = launch {
        lifecycle.whenResumed(block)
    }*/
}