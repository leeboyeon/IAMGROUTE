package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserPlanApi {
    @GET("/plan/{id}")
    suspend fun getUserPlanById(@Path("id") id: Int) : Response<Map<String,Any>>

    @GET("/plan/best")
    suspend fun getBestUserPlan() : Response<MutableList<UserPlan>>

    @POST("/plan/insert")
    fun insertUserPlan(@Query("planId")planId:Int, @Body userPlan:UserPlan, @Query("userIds") userIds:List<String>) : Call<Boolean>

    @GET("/plan/list/{userId}")
    suspend fun getMyUserPlan(@Path("userId")userId:String):Response<MutableList<UserPlan>>

    @GET("/plan/list/notend/{userId}")
    suspend fun getMyPlanNotEnd(@Path("userId")userId:String) : Response<MutableList<UserPlan>>

    @GET("/plan/list/end/{userId}")
    suspend fun getMyPlanEnd(@Path("userId") userId:String) : Response<MutableList<UserPlan>>

    @GET("/plan/review/list/{planId}")
    suspend fun getPlanReviewListbyId(@Path("planId") planId:Int) : Response<MutableList<PlanReview>>

    @POST("/plan/review")
    fun insertPlanReview(@Body planReview: PlanReview):Call<Boolean>

    @DELETE("/plan/review/del")
    fun deletePlanReview(@Query("id")id:Int):Call<Boolean>

    @PUT("/plan/review/update")
    fun updatePlanReview(@Body planReview: PlanReview) : Call<Boolean>

    @GET("/plan/review/detail")
    suspend fun getPlanReviewbyId(@Query("id")id:Int): Response<PlanReview>

    @POST("/plan/place")
    fun insertPlaceToUserPlan(@Body routeDetail: RouteDetail) : Call<Boolean>

    @GET("/plan/list")
    suspend fun getPlanList() : Response<MutableList<UserPlan>>

    @POST("/plan/isLike")
    fun planIsLike(@Body planLike: PlanLike) : Call<Boolean>

    @POST("/plan/like")
    fun planLike(@Body planLike: PlanLike) : Call<Boolean>

    @GET("/plan/like/{userId}")
    suspend fun getPlanLikeListbyUserId(@Path("userId")userId:String) : Response<MutableList<UserPlan>>

    @PUT("/plan/place/priority")
    fun updatePriority(@Body routeDetailList:List<RouteDetail>) : Call<Boolean>

}