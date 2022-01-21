package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Category
import retrofit2.Call
import retrofit2.http.GET

interface AreaApi {
    @GET("/area/list")
    fun listArea() : Call<List<Category>>
}