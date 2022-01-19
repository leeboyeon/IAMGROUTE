package com.ssafy.groute.config

import android.app.Application
import com.ssafy.groute.util.SharedPreferencesUtil
import com.ssafy.smartstore.src.main.intercepter.AddCookiesInterceptor
import com.ssafy.smartstore.src.main.intercepter.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "ApplicationClass_Groute"
class ApplicationClass : Application() {
    companion object{
//        const val SERVER_URL = "http://172.30.1.19:8888/"   // Kyunghee
        const val SERVER_URL = "http://61.85.38.39:8888/"   // Jiwoo
        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit

    }

    override fun onCreate() {
        super.onCreate()
        //shared preference 초기화
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AddCookiesInterceptor())
            .addInterceptor(ReceivedCookiesInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS).build()

        retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

}