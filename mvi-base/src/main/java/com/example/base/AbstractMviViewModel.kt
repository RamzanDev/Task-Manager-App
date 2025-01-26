package com.example.base

import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.ui.debugCheckImmediateMainDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

private fun debugCheckMainThread() {
    check(Looper.getMainLooper() == Looper.myLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}

abstract class AbstractMviViewModel<
        I : MviIntent,
        S : MviViewState,
        E : MviSingleEvent
        > : MviViewModel<I, S, E>, ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main.immediate
    protected open val rawLogTag: String? = null

    protected val logTag by lazy(LazyThreadSafetyMode.PUBLICATION) {
        (rawLogTag ?: this::class.java.simpleName).let { tag: String ->
            tag
        }
    }

    private val eventChannel = Channel<E>(Channel.UNLIMITED)
    private val intentMutableFlow = MutableSharedFlow<I>(extraBufferCapacity = Int.MAX_VALUE)

    override val singleEvent: Flow<E> = eventChannel.receiveAsFlow()

    @MainThread
    final override suspend fun processIntent(intent: I) {
        debugCheckMainThread()
        debugCheckImmediateMainDispatcher()

        check(intentMutableFlow.tryEmit(intent)) { "Failed to emit intent: $intent" }
        Timber.tag(logTag).d("processIntent: $intent")
    }

    /**
     * Send event and access intent flow.
     * Must be called in [kotlinx.coroutines.Dispatchers.Main.immediate]
     */

    protected suspend fun sendEvent(event: E) {
        debugCheckMainThread()
        debugCheckImmediateMainDispatcher()

        eventChannel.trySend(event)
            .onSuccess { Timber.tag(logTag).d("sendEvent: event = $event") }
            .onFailure {
                Timber.tag(logTag).e(it, "Failed to send event: $event")
            }
            .getOrThrow()
    }

    protected val intentSharedFlow: SharedFlow<I> get() = intentMutableFlow

    /**
     * Extensions on Flow using viewModelScope
     */

    protected fun <T> Flow<T>.debugLog(subject: String): Flow<T> =
        if (true) {
            onEach { Timber.tag(logTag).d(">>> $subject: $it") }
        } else {
            this
        }

    protected fun <T> SharedFlow<T>.debugLog(subject: String): SharedFlow<T> =
        if (true) {
            val self = this

            object : SharedFlow<T> by self {
                val subcriberCount = AtomicInteger(0)

                override suspend fun collect(collector: FlowCollector<T>): Nothing {
                    val count = subcriberCount.getAndIncrement()

                    self.collect {
                        Timber.tag(logTag).d(">>> $subject ~ $count: $it")
                        collector.emit(it)
                    }
                }
            }
        } else {
            this
        }


    /**
     * Share the flow in [viewModelScope],
     * start when the first subscriber arrives,
     * and stop when the last subscriber leaves.
     */

    protected fun <T> Flow<T>.shareWhileSubscribed(): SharedFlow<T> =
        shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    protected fun <T> Flow<T>.stateWithInitialNullWhileSubscribed(): StateFlow<T?> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

}