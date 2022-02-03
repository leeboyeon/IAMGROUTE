package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.response.PlaceLikeResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface PlaceApi {
    /**
     * 지금은 place 테이블에 제주도 장소 뿐이지만 후에 다른 지역 장소가 추가된다면
     * place 호출 부분 나중에 areaId로 각 지역별 장소 호출할 수 있도록 변경하기
     * 서버 + android 둘 다 수정해야 됨.
     */

    // 제주도 지역 장소
    @GET("/place/list")
    fun listPlace() : Call<List<Place>>

    // 제주도 지역 장소 중 1개
    @GET("/place/detail")
    fun getPlace(@Query("id") id:Int) : Call<Place>


    @PUT("/place/update")
    fun updatePlace(@Body place:Place) : Call<Boolean>


    // 제주도 지역 장소 - Coroutine test
    @GET("/place/list")
    suspend fun getPlaceList() : Response<MutableList<Place>>

    //id로  place객체 찾기(코루틴버전)
    @GET("/place/detail")
    suspend fun getPlacebyId(@Query("id") id:Int) : Response<Place>

    //좋아요 순 많은순으로 정렬하여 top 5 찾기
    @GET("/place/best")
    suspend fun getPlaceBestList() : Response<MutableList<Place>>

    //place 좋아요 혹은 취소
    @POST("/place/like")
    fun placeLike(@Body placelike:PlaceLikeResponse) : Call<Boolean>

    //place 좋아요 했는지
    @POST("/place/isLike")
    fun placeIsLike(@Body placelike: PlaceLikeResponse) : Call<Boolean>

    @GET("/place/like/{userId}")
    suspend fun getPlaceLikeListbyUserId(@Path("userId")userId:String) : Response<MutableList<Place>>
}