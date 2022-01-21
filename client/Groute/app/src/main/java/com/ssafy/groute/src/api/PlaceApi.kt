package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.home.Place
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceApi {
    @GET("/place/list")
    fun listPlace() : Call<List<Places>>

    @GET("/place/detail")
    fun getPlace(@Query("id") id:Int) : Call<Places>
}