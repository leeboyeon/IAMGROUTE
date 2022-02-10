package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AccountApi {

    @GET("/account/list/{userPlanId}")
    suspend fun getListByPlanId(@Path("userPlanId")userPlanId:Int):Response<MutableList<Account>>

    @POST("/account/insert")
    fun insertAccount(@Body account: Account) : Call<Boolean>

    @GET("/account/category")
    suspend fun getCategoryList():Response<MutableList<AccountCategory>>

    @GET("/account/priceByCategory")
    suspend fun getCategoryChart(@Query("planId")planId:Int) : Response<Map<Int, Int>>

}