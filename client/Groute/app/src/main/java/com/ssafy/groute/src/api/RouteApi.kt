package com.ssafy.groute.src.api


import com.ssafy.groute.src.dto.Route
import retrofit2.Call
import retrofit2.http.*

interface RouteApi {
    /*
    * ID로 route 가져오기
    * */
    @GET("route/list")
    fun getRoutebyId(@Query("id")id:Int) : Call<MutableList<Route>>
    /*
    * route 추가하기
    * */
    @POST("/route/insert")
    fun insertRoute(@Body route:Route) : Call<Boolean>
    /*
    * 모든 route List 가져오기*/
    @GET("/route/list")
    fun getRouteList():Call<MutableList<Route>>
    /*
    *route 삭제하기 */
    @DELETE("route/del")
    fun deleteRoute(@Query("id") id:Int) : Call<Boolean>
    /*
    * route 수정*
     */
    @PUT("route/update")
    fun updateRoute(@Body route:Route) : Call<Boolean>
}