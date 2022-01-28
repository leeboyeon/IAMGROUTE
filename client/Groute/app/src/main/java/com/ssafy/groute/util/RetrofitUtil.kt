package com.ssafy.groute.util

import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.src.api.*

class RetrofitUtil {
    companion object{
        val userService = ApplicationClass.retrofit.create(UserApi::class.java)
        val areaService = ApplicationClass.retrofit.create(AreaApi::class.java)
        val placeService = ApplicationClass.retrofit.create(PlaceApi::class.java)
        val boardService = ApplicationClass.retrofit.create(BoardApi::class.java)
        val commentService = ApplicationClass.retrofit.create(CommentApi::class.java)
        val routeService = ApplicationClass.retrofit.create(RouteApi::class.java)
        val routeDetailService = ApplicationClass.retrofit.create(RouteDetailApi::class.java)
    }
}