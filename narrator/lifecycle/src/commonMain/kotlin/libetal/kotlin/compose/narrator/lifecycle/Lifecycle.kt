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
import libetal.kotlin.debug.debug
import libetal.kotlin.laziest
import libetal.kotlin.debug.info
import kotlin.coroutines.CoroutineContext
import libetal.kotlin.coroutines.IO as CompatIO


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
abstract class Lifecycle(internal var deathDate: Long = 5000L) {
    val isPausing
        get() = pauseJob != null

    var pauseJob: Job? = null
        internal set(value) {

            if (field != null && value != null) return value.cancel()

            if (value == null) field?.cancel()

            field = value

        }

    private val observers by laziest {
        mutableListOf<Observer>()
    }

    private val lambdaObservers by laziest {
        mutableMapOf<String, (state: State) -> Unit>()
    }

    val wasCreated
        get() = state > State.CREATED && !isPaused && !isDestroyed


    /**@Description
     * Defines the initial state of the component
     * */
    var state: State = State.DESTROYED
        internal set(value) {

            if (field.ordinal == value.ordinal) return

            field = value

            onStateChange(state)
        }

    val isPaused: Boolean
        get() {
            return state == State.PAUSED
        }

    val isDestroyed: Boolean
        get() = state == State.DESTROYED


    fun addObserver(observer: Observer): Boolean {
        if (observer in observers) return false
        return observers.add(observer)
    }

    fun addObserver(observer: (State) -> Unit) {
        lambdaObservers[observer.hashCode().toString()] = observer
    }

    fun removeObserver(observer: Observer): Boolean = observers.remove(observer)

    private fun transition(state: State, delay: Long) = ioLaunch {
        delay(delay)
        launch {
            this@Lifecycle.state = state
        }
    }

    val coroutineScope: CoroutineScope by laziest {
        CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    val ioScope: CoroutineScope by laziest {
        CloseableCoroutineScope(SupervisorJob() + Dispatchers.CompatIO as CoroutineContext)
    }

    protected open fun onStateChange(state: State) {
        when (state) {
            State.CREATED -> {
                pauseJob = null
                TAG debug "Creating... ${this::class.simpleName}"
            }

            State.RESUMED -> {
                pauseJob = null
                TAG debug "Resuming... ${this::class.simpleName}: $state"
            }

            State.PAUSED -> {
                pauseJob = transition(State.DESTROYED, deathDate)
                TAG debug "Pausing ${this::class.simpleName}..."
            }

            State.DESTROYED -> {
                ioScope.cancel()
                coroutineScope.cancel()
                TAG debug "Destroyed... ${this::class.simpleName}"
            }
        }

        observers.forEach {
            it.onStateChange(state)
            TAG debug "Calling state change for ${this::class.simpleName} $state"
        }

        lambdaObservers.forEach { (_, func) ->
            func(state)
        }

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
    ) = ioScope.launch(start = start, block = block)

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
        // STARTED,

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
                RESUMED -> PAUSED
                PAUSED -> DESTROYED
                DESTROYED -> DESTROYED // TODO move to CREATED not sure of side effects
            }


    }

    interface Callbacks {
        fun onCreate() {
        }

        fun onResume() {}

        fun onPause() {}

        fun onDestroy() {}

    }

    interface Owner<L : Lifecycle> {

        val lifeCycle: L

        fun create() {
            lifeCycle.state = State.CREATED
        }

        fun resume() {
            lifeCycle.state = State.RESUMED
        }

        fun pause(killAfter: Long = lifeCycle.deathDate) {
            // No point in pausing infinite viewModels
            if (killAfter == Long.MAX_VALUE) return
            if (lifeCycle.isPausing || lifeCycle.isPaused) return

            lifeCycle.state = State.PAUSED

            lifeCycle.deathDate = killAfter

        }

    }

    interface Observer {
        fun onStateChange(state: State)
    }

    companion object {
        private const val TAG = "LifeCycle"
    }

}


