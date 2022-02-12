package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Notification
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {

    @POST("/notification/insert")
    fun insertNotification(@Body req: Notification) : Call<Boolean>

    @GET("/notification/detail")
    fun getNotificationById(@Query("id") id: Int) : Call<Notification>

    @GET("/notification/detailList")
    suspend fun getNotificationByUserId(@Query("userId") userId: String) : Response<MutableList<Notification>>

    @DELETE("/notification/del")
    fun deleteNotification(@Query("id") id: Int) : Call<Boolean>

    @PUT("/notification/update")
    fun updateNotification(@Body notification: Notification) : Call<Boolean>

}