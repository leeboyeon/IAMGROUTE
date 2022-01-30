package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Place
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

}