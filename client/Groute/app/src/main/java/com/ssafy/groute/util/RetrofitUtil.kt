package com.ssafy.groute.util

import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.api.UserApi

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
    }
}