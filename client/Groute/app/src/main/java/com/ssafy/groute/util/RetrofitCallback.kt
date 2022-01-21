package com.ssafy.groute.util

import com.ssafy.groute.src.dto.Category

interface RetrofitCallback<T> {
    fun onError(t: Throwable)

    fun onSuccess(code: Int, responseData: T)

    fun onFailure(code: Int)
}