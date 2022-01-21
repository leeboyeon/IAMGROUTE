package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.home.Place
import retrofit2.Call
import retrofit2.http.GET

interface PlaceApi {
    @GET("/place/list")
    fun listPlace() : Call<List<Places>>
}