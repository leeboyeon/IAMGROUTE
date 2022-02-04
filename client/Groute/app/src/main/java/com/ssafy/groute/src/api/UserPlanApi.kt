package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.UserPlan
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserPlanApi {
    @GET("/plan/{id}")
    fun getUserPlanById(@Query("id") id: Int) : Call<MutableList<UserPlan>>

    @GET("/plan/best")
    suspend fun getBestUserPlan() : Response<MutableList<UserPlan>>

    @POST("/plan/insert")
    fun insertUserPlan(@Query("planId")planId:Int, @Body userPlan:UserPlan, @Query("userIds") userIds:List<String>) : Call<Boolean>

    @GET("/plan/list/{userId}")
    suspend fun getMyUserPlan(@Path("userId")userId:String):Response<MutableList<UserPlan>>
}