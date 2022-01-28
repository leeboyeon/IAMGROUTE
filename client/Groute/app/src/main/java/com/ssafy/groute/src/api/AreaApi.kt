package com.ssafy.groute.src.api

import androidx.lifecycle.LiveData
import com.ssafy.groute.src.dto.Area
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface AreaApi {
    @GET("/area/list")
    fun listArea() : Call<List<Area>>

    @GET("/area/list")
    suspend fun listAreas() : Response<MutableList<Area>>
}