package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.src.dto.AccountCategory
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApi {

    @GET("/account/list/{userPlanId}")
    suspend fun getListByPlanId(@Path("userPlanId")userPlanId:Int):Response<MutableList<Account>>

    @POST("/account/insert")
    fun insertAccount(@Body account: Account) : Call<Boolean>

    @GET("/account/category")
    suspend fun getCategoryList():Response<MutableList<AccountCategory>>

}