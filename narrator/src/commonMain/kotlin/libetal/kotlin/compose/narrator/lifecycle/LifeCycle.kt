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
import libetal.kotlin.compose.narrator.coroutines.IoDispatcher

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


    private val supervisorJob
        get() =
            SupervisorJob()

    private val coroutineScopes  by lazy{
        mutableListOf<CoroutineScope>()
    }

    private fun getCoroutineScope(context: CoroutineDispatcher = Dispatchers.Main.immediate, supervisorJob: CompletableJob?) =
        CoroutineScope(supervisorJob?.let { it + context } ?: context).also {
            coroutineScopes += it
        }

    fun launch(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        parent: Job? = null,
        supervisorJob: CompletableJob? = SupervisorJob(parent),
        block: suspend CoroutineScope.() -> Unit
    ): CoroutineScope {
        val coroutineScope = getCoroutineScope(dispatcher, supervisorJob)

        /*when (state) {
            State.DESTROYED -> context.cancel()
            State.INITIALIZED -> TODO()
            State.CREATED -> TODO()
            State.STARTED -> context.launch(block = block)
            State.RESUMED -> TODO()
        }*/

        coroutineScope.launch(block = block)

        return coroutineScope
    }


    fun ioLaunch(
        parent: Job? = null,
        supervisorJob: CompletableJob? = SupervisorJob(parent),
        block: suspend CoroutineScope.() -> Unit
    ) = launch(
        IoDispatcher,
        parent,
        supervisorJob,
        block
    )

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
    //   @MainThread
    fun addObserver(observer: LifecycleObserver) {

    }

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
    //    @MainThread
    fun removeObserver(observer: LifecycleObserver) {

    }

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
        ON_STOP,

        /**
         * Constant for onDestroy event of the {@link LifecycleOwner}.
         */
        ON_DESTROY,

        /**
         * An {@link Event Event} constant that can be used to match all events.
         */
        ON_ANY;


        companion object {

            /**
             * Returns the {@link Lifecycle.Event} that will be reported by a {@link Lifecycle}
             * leaving the specified {@link Lifecycle.State} to a lower state, or {@code null}
             * if there is no valid event that can move down from the given state.
             *
             * @param state the higher state that the returned event will transition down from
             * @return the event moving down the lifecycle phases from state
             */
            public fun downFrom(state: State): Event? {
                return when (state) {
                    State.CREATED -> ON_DESTROY
                    State.STARTED -> ON_STOP
                    State.RESUMED -> ON_PAUSE
                    else -> null
                }
            }


            public fun downTo(state: State): Event? {
                return when (state) {
                    State.DESTROYED -> ON_DESTROY
                    State.CREATED -> ON_STOP
                    State.STARTED -> ON_PAUSE
                    else -> null
                }
            }


            public fun upFrom(state: State): Event? = when (state) {
                State.INITIALIZED -> ON_CREATE
                State.CREATED -> ON_START
                State.STARTED -> ON_RESUME
                else -> null
            }

            /**
             * Returns the {@link Lifecycle.Event} that will be reported by a {@link Lifecycle}
             * entering the specified {@link Lifecycle.State} from a lower state, or {@code null}
             * if there is no valid event that can move up to the given state.
             *
             * @param state the higher state that the returned event will transition up to
             * @return the event moving up the lifecycle phases to state
             */

            fun upTo(state: State) = when (state) {
                State.CREATED -> ON_CREATE
                State.STARTED -> ON_START
                State.RESUMED -> ON_RESUME
                else -> null
            }


            /**
             * Returns the new {@link Lifecycle.State} of a {@link Lifecycle} that just reported
             * this {@link Lifecycle.Event}.
             *
             * Throws {@link IllegalArgumentException} if called on {@link #ON_ANY}, as it is a special
             * value used by {@link OnLifecycleEvent} and not a real lifecycle event.
             *
             * @return the state that will result from this event
             */
            fun Event.getTargetState(): State = when (this) {
                ON_CREATE, ON_STOP -> State.CREATED
                ON_START, ON_PAUSE -> State.STARTED
                ON_RESUME -> State.RESUMED
                ON_DESTROY -> State.DESTROYED
                // ON_ANY: ->
                else -> throw  IllegalArgumentException("$this has no target state");
            }


        }

    }

    enum class State {
        /**
         * Destroyed state for a LifecycleOwner. After this event, this Lifecycle will not dispatch
         * any more events. For instance, for an {@link android.app.Activity}, this state is reached
         * <b>right before</b> Activity's {@link android.app.Activity#onDestroy() onDestroy} call.
         */
        DESTROYED,

        /**
         * Initialized state for a LifecycleOwner. For an {@link android.app.Activity}, this is
         * the state when it is constructed but has not received
         * {@link android.app.Activity#onCreate(android.os.Bundle) onCreate} yet.
         */
        INITIALIZED,

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
         * Started state for a LifecycleOwner. For an {@link android.app.Activity}, this state
         * is reached in two cases:
         * <ul>
         *     <li>after {@link android.app.Activity#onStart() onStart} call;
         *     <li><b>right before</b> {@link android.app.Activity#onPause() onPause} call.
         * </ul>
         */
        STARTED,

        /**
         * Resumed state for a LifecycleOwner. For an {@link android.app.Activity}, this state
         * is reached after {@link android.app.Activity#onResume() onResume} is called.
         */
        RESUMED;

        /**
         * Compares if this State is greater or equal to the given {@code state}.
         *
         * @param state State to compare with
         * @return true if this State is greater or equal to the given {@code state}
         */
        public fun isAtLeast(state: State) = compareTo(state) >= 0

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

        fun onStop() {
        }

        fun onDestroy() {
        }

    }
}


