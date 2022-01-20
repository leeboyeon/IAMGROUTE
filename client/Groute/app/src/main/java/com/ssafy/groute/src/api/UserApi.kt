package com.ssafy.groute.src.api
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {

    // 사용자 로그인
    @GET("/user/login")
    fun login(@Query("id") id: String, @Query("password") password: String): Call<User>

    // id 중복 확인
    @GET("/user/isUsedId")
    fun isUsedId(@Query("id") id : String) : Call<Boolean>

    // 사용자의 아이디에 해당하는 사용자 정보를 반환
    @GET("/user/{userId}")
    fun getUserInfo(@Path("userId") userId: String): Call<UserInfoResponse>


}