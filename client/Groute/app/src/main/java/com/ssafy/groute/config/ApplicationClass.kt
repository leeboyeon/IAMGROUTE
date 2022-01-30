package com.ssafy.groute.config

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.groute.R
import com.ssafy.groute.config.intercepter.AddCookiesInterceptor
import com.ssafy.groute.config.intercepter.ReceivedCookiesInterceptor
import com.ssafy.groute.config.intercepter.XAccessTokenInterceptor
import com.ssafy.groute.util.SharedPreferencesUtil
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_Groute"
class ApplicationClass : Application() {
    companion object{
//        const val SERVER_URL = "http://172.30.1.19:8888/"   // Kyunghee
//        const val SERVER_URL = "http://61.85.38.39:8888/"   // Jiwoo
//        const val SERVER_URL = "http://172.30.1.42:8888/"   // Jiwoo
        //AWS servoer
        const val SERVER_URL = "http://i6d109.p.ssafy.io:8888/"
        const val IMGS_URL_USER = "${SERVER_URL}imgs/user/"
        const val IMGS_URL_AREA = "${SERVER_URL}imgs/area/"
        const val IMGS_URL_PLACE = "${SERVER_URL}imgs/place"
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

        // JWT Token Header 키 값
        const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"

    }

    override fun onCreate() {
        super.onCreate()
        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS).build()

        // Gson 객체 생성 - setLenient 속성 추가
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        
        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_nativeapp_key))
    }

}