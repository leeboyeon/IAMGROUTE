package com.ssafy.groute.util

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@JvmName("awaitNullable")
suspend fun <T : Any> Call<T?>.await(): T? {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation {
            cancel()
        }
        enqueue(object : Callback<T?> {
            override fun onResponse(call: Call<T?>, response: Response<T?>) {
                if (response.isSuccessful) {
                    continuation.resume(response.body())
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }

            override fun onFailure(call: Call<T?>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }
}