package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Places
import retrofit2.Call
import retrofit2.http.*

interface PlaceApi {
    @GET("/place/list")
    fun listPlace() : Call<List<Places>>

    @GET("/place/detail")
    fun getPlace(@Query("id") id:Int) : Call<Places>

    @PUT("/place/update")
    fun updatePlace(@Body place:Places) : Call<Boolean>
}