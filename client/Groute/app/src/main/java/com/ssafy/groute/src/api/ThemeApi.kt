package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Theme
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ThemeApi {
    @GET("/theme/detail")
    suspend fun getThemeById(@Query("id") id : Int) : Response<Theme>

    @GET("/theme/list")
    suspend fun getThemeList() : Response<MutableList<Theme>>
}