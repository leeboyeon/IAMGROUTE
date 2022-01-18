package com.ssafy.groute.src.api
import com.ssafy.groute.src.dto.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    // 사용자 로그인
    @GET("/user/login")
    fun login(@Query("id") id: String, @Query("password") password: String): Call<User>

    // id 중복 확인
    @GET("/user/isUsedId")
    fun isUsedId(@Query("id") id : String) : Call<Boolean>
}