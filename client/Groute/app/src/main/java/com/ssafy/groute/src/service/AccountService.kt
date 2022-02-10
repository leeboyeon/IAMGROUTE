package com.ssafy.groute.src.service

import android.util.Log
import com.ssafy.groute.src.dto.Account
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "AccountService"
class AccountService {
    suspend fun getListByPlanId(userPlanId:Int) : Response<MutableList<Account>>{
        return RetrofitUtil.accountService.getListByPlanId(userPlanId)
    }

    fun insertAccount(account: Account, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.accountService.insertAccount(account).enqueue(object :Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if (response.code() == 200) {
                    if (res == true) {
                        callback.onSuccess(response.code(), res)
                        Log.d(TAG, "onResponse: ")
                    } else {
                        Log.d(TAG, "onResponse: ")
                    }
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    suspend fun getCategoryList() = RetrofitUtil.accountService.getCategoryList()

    suspend fun getCategoryChart(planId:Int) : Response<Map<Int,Int>>{
        return RetrofitUtil.accountService.getCategoryChart(planId)
    }
}