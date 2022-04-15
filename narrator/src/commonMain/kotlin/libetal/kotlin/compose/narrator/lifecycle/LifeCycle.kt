/*
 * Copyright (C) 2017 The Android Open Source Project
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

import kotlinx.coroutines.*
import libetal.kotlin.compose.narrator.coroutines.IO
import libetal.kotlin.debug.debug
import libetal.kotlin.debug.info
import kotlin.coroutines.CoroutineContext


/**
 * Defines an object that has an Android Lifecycle. {@link androidx.fragment.app.Fragment Fragment}
 * and {@link androidx.fragment.app.FragmentActivity FragmentActivity} classes implement
 * {@link LifecycleOwner} interface which has the {@link LifecycleOwner#getLifecycle()
 * getLifecycle} method to access the Lifecycle. You can also implement {@link LifecycleOwner}
 * in your own classes.
 * <p>
 * {@link Event#ON_CREATE}, {@link Event#ON_START}, {@link Event#ON_RESUME} events in this class
 * are dispatched <b>after</b> the {@link LifecycleOwner}'s related method returns.
 * {@link Event#ON_PAUSE}, {@link Event#ON_STOP}, {@link Event#ON_DESTROY} events in this class
 * are dispatched <b>before</b> the {@link LifecycleOwner}'s related method is called.
 * For instance, {@link Event#ON_START} will be dispatched after
 * {@link android.app.Activity#onStart onStart} returns, {@link Event#ON_STOP} will be dispatched
 * before {@link android.app.Activity#onStop onStop} is called.
 * This gives you certain guarantees on which state the owner is in.
 * <p>
 * To observe lifecycle events call {@link #addObserver(LifecycleObserver)} passing an object
 * that implements either {@link DefaultLifecycleObserver} or {@link LifecycleEventObserver}.
 */
abstract class Lifecycle {

    /**@Description
     * Defines the initial state of the component
     * */
    var state: State = State.DESTROYED
        set(value) {

            TAG debug "${this::class.qualifiedName} Field = $field NewValue = $value"

            if (field.ordinal == value.ordinal) return

            field = value

            onStateChange(value)

        }

    val isPaused: Boolean
        get() {
            return state == State.PAUSED
        }

    val isDestroyed: Boolean
        get() = state == State.DESTROYED

    val coroutineScope: CoroutineScope by lazy {
        CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main).also { coroutineScope ->
            coroutineScopes += coroutineScope
        }
    }

    private val supervisorJob
        get() = SupervisorJob()

    private val coroutineScopes by lazy {
        mutableListOf<CoroutineScope>()
    }

    protected abstract fun onStateChange(state: State)

    /**
     * TODO
     * remove implementation as a LifeCycle
     * just has a single scope currently
     **/
    private fun getCoroutineScope(context: CoroutineDispatcher = Dispatchers.Main, supervisorJob: CompletableJob?) =
        CoroutineScope(supervisorJob?.let { it + context } ?: context).also {
            coroutineScopes += it
        }

    fun launch(
        coroutineScope: CoroutineScope = this.coroutineScope,
        coroutineContext: CoroutineContext = coroutineScope.coroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = coroutineScope.launch(coroutineContext, block = block)

    fun launch(
        dispatcher: CoroutineDispatcher,
        coroutineScope: CoroutineScope = this.coroutineScope,
        block: suspend CoroutineScope.() -> Unit
    ) = coroutineScope.launch(dispatcher, block = block)

    fun ioLaunch(
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = coroutineScope.launch(Dispatchers.IO, start = start, block = block)

    /**
     * Adds a LifecycleObserver that will be notified when the LifecycleOwner changes
     * state.
     * <p>
     * The given observer will be brought to the current state of the LifecycleOwner.
     * For example, if the LifecycleOwner is in {@link State#STARTED} state, the given observer
     * will receive {@link Event#ON_CREATE}, {@link Event#ON_START} events.
     *
     * @param observer The observer to notify.
     */
    abstract fun addObserver(observer: Observer): Boolean

    /**
     * Removes the given observer from the observers list.
     * <p>
     * If this method is called while a state change is being dispatched,
     * <ul>
     * <li>If the given observer has not yet received that event, it will not receive it.
     * <li>If the given observer has more than 1 method that observes the currently dispatched
     * event and at least one of them received the event, all of them will receive the event and
     * the removal will happen afterwards.
     * </ul>
     *
     * @param observer The observer to be removed.
     */
    abstract fun removeObserver(observer: Observer): Boolean

    enum class Event {
        /**
         * Constant for onCreate event of the {@link LifecycleOwner}.
         */
        ON_CREATE,

        /**
         * Constant for onStart event of the {@link LifecycleOwner}.
         */
        ON_START,

        /**
         * Constant for onResume event of the {@link LifecycleOwner}.
         */
        ON_RESUME,

        /**
         * Constant for onPause event of the {@link LifecycleOwner}.
         */
        ON_PAUSE,

        /**
         * Constant for onStop event of the {@link LifecycleOwner}.
         */
        /*ON_STOP,*/

        /**
         * Constant for onDestroy event of the {@link LifecycleOwner}.
         */
        ON_DESTROY,

        /**
         * An {@link Event Event} constant that can be used to match all events.
         */
        ON_ANY;


        companion object {

        }

    }

    enum class State {

        /**
         * Created state for a LifecycleOwner. For an {@link android.app.Activity}, this state
         * is reached in two cases:
         * <ul>
         *     <li>after {@link android.app.Activity#onCreate(android.os.Bundle) onCreate} call;
         *     <li><b>right before</b> {@link android.app.Activity#onStop() onStop} call.
         * </ul>
         */
        CREATED,


        /**
         * Resumed state for a LifecycleOwner. For an {@link android.app.Activity}, this state
         * is reached after {@link android.app.Activity#onResume() onResume} is called.
         */
        RESUMED,

        /**
         * Started state for a LifecycleOwner. For an {@link android.app.Activity}, this state
         * is reached in two cases:
         * <ul>
         *     <li>after {@link android.app.Activity#onStart() onStart} call;
         *     <li><b>right before</b> {@link android.app.Activity#onPause() onPause} call.
         * </ul>
         */
        STARTED,

        /**
         * Intermediate state awaiting destruction
         **/
        PAUSED,

        /**
         * Destroyed state for a LifecycleOwner. After this event, this Lifecycle will not dispatch
         * any more events. For instance, for an {@link android.app.Activity}, this state is reached
         * <b>right before</b> Activity's {@link android.app.Activity#onDestroy() onDestroy} call.
         */
        DESTROYED;


        /**
         * Compares if this State is greater or equal to the given {@code state}.
         *
         * @param state State to compare with
         * @return true if this State is greater or equal to the given {@code state}
         */
        fun isAtLeast(state: State) = compareTo(state) >= 0

        val nextState
            get() = when (this) {
                CREATED -> RESUMED
                RESUMED -> STARTED
                STARTED -> STARTED
                PAUSED -> DESTROYED
                DESTROYED -> DESTROYED // TODO move to CREATED not sure of side effects
            }


    }

    interface Callbacks {

        fun onCreate() {
        }

        fun onStart() {
        }

        fun onResume() {
        }

        fun onPause() {
        }

        /*      fun onStop() {
              }*/

        fun onDestroy() {
        }

    }

    interface Observer

    interface Owner<L : Lifecycle> {
        /**
         * Returns the Lifecycle of the provider.
         *
         * @return The lifecycle of the provider.
         */
        // val lifeCycle: L
    }

    companion object {
        private const val TAG = "LifeCycle"
    }

}


