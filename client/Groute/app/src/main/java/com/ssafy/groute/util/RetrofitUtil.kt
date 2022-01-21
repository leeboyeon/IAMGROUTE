package com.ssafy.groute.util

import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.api.AreaApi
import com.ssafy.groute.src.api.UserApi

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val areaService = ApplicationClass.retrofit.create(AreaApi::class.java)
    }
}