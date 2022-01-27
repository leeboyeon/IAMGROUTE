package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.RouteDetail
import retrofit2.Call
import retrofit2.http.*

interface RouteDetailApi {
    /*
    * routeDetail ID로 삭제하기
    * */
    @DELETE("routeDetail/del")
    fun deleteRouteDetail(@Query("id")id:Int) : Call<Boolean>
    /*
    * routeDetail id로 객체꺼내기
    * */
    @GET("routeDetail/detail")
    fun getRouteDetailbyId(@Query("id") id:Int) : Call<RouteDetail>

    /*
    * routeDetail 추가하기
    * */
    @POST("routeDetail/insert")
    fun insertRouteDetail(@Body routeDetail:RouteDetail) : Call<Boolean>

    /*
    * routeDetail 모든 리스트 출력하기
    * */
    @GET("routeDeatil/list")
    fun getRouteDetailList() : Call<MutableList<RouteDetail>>

    /*
    * routeDetail 수정하기
    * */
    @PUT("routeDetail/update")
    fun updateRouteDetail(@Body routeDetail:RouteDetail) : Call<Boolean>


}