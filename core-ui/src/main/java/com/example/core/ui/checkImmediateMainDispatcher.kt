package com.example.core.ui

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.ContinuationInterceptor

suspend fun debugCheckImmediateMainDispatcher() {
    val interceptor = currentCoroutineContext()[ContinuationInterceptor]
    Log.d(
        "###",
        "debugCheckImmediateMainDispatcher: $interceptor, ${Dispatchers.Main.immediate}, ${Dispatchers.Main}"
    )

    check(interceptor == Dispatchers.Main.immediate) {
        "Excepted ContinuationInterceptor to be Dispatchers.Main but was $interceptor"
    }
}