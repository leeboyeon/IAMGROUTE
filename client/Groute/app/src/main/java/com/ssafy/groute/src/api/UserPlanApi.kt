package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.UserPlan
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserPlanApi {
    @GET("/plan/{id}")
    fun getUserPlanById(@Query("id") id: Int) : Call<MutableList<UserPlan>>

    @GET("/plan/best")
    suspend fun getBestUserPlan() : Response<MutableList<UserPlan>>

}