package com.ssafy.groute.src.api
import android.content.Entity
import com.ssafy.groute.src.dto.SharedUser
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.response.UserInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

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

    // 사용자의 아이디에 해당하는 사용자 정보를 반환 - coroutine ver.
    @GET("/user/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserInfoResponse>

    // 회원가입
    @POST("/user/signup")
    fun signUp(@Body user:User) : Call<Boolean>

    // 사용자 정보 수정 - 이미지 X
//    @Multipart
//    @PUT("/user/update")
//    fun updateUser(@Part("user") user : RequestBody): Call<Boolean>
    
    // 사용자 정보 수정 - 사용자 정보 + 이미지
    @Multipart
    @PUT("/user/update")
    fun updateUser(@Part("user") user : RequestBody, @Part img: MultipartBody.Part?): Call<Boolean>

    @DELETE("/user/{userId}")
    fun deleteUser(@Path("userId") userId:String) : Call<Boolean>

    // 사용자 이메일로 아이디 조회
    @GET("/user/id/{email}")
    fun getUserIdByEmail(@Path("email") email: String): Call<UserInfoResponse>

    // 사용자 이메일 아이디로 맞는거 있는지 조회
    @GET("/user/pwd")
    fun isUserEmailAndId(@Query("email") email: String, @Query("id") id: String): Call<Boolean>

    // 비밀번호 변경
    @PUT("/user/update/password")
    fun updateUserPassword(@Body userInfo: UserInfoResponse) : Call<Boolean>

    //SharedUser

    @POST("/planShareUser/insert")
    fun insertSharedUser(@Body shareUser:SharedUser) : Call<Boolean>

    @PUT("/planShareUser/update")
    fun updateSharedUser(@Body shareUser:SharedUser) : Call<Boolean>

    @POST("/planShareUser/del")
    fun deleteSharedUser(@Body shareUser:SharedUser) : Call<Boolean>

    @GET("planShareUser/list/{planId}")
    suspend fun getShareUserList(@Path("planId")planId:Int) : Response<MutableList<User>>
}