package com.ssafy.groute.src.api
import android.content.Entity
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

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



    // 회원가입
    @POST("/user/signup")
    fun signUp(@Body user:User) : Call<Boolean>
}