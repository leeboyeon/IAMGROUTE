package com.ssafy.groute.src.service

import android.util.Log
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HTTP
import java.util.function.BiPredicate

class UserService {
    fun login(user: User, callback: RetrofitCallback<User>) {
        RetrofitUtil.userService.login(user.id, user.password).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onError(t)
            }
        })
    }


    /**
     * 서버로부터 입력된 id가 사용 중인지 확인한다.
     * @param inputId
     * @param callback
     */
    fun isUsedId(inputId: String, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userService.isUsedId(inputId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    /**
     * #S06P12D109-24 회원가입
     * 사용자로 부터 입력받은 사용자 데이터를 서버로 전송한다.
     * @param user
     * @return callback
     */
    fun signUp(user: User, callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.userService.signUp(user).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                Log.d("UserService_싸싸피", "onResponse: $response.")
                if(response.code() == 200) {
                    if (res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

}