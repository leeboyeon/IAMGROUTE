package com.ssafy.groute.src.api

import retrofit2.Call
import retrofit2.http.*

interface FirebaseTokenService {

    // Token정보 서버로 전송
    @POST("/fcm/token")
    fun uploadToken(@Query("token") token: String, @Query("userId") userId: String): Call<Boolean>

    @POST("fcm/sendMessageTo")
    fun sendMessageTo(
        @Query("token") token: String,
        @Query("title") title: String,
        @Query("body") body: String,
        @Query("path") path: String
    ): Call<Boolean>

    @POST("fcm/broadcast")
    fun broadCast(@Query("title") title: String, @Query("body") body: String, @Query("path") path: String) : Call<Boolean>
}