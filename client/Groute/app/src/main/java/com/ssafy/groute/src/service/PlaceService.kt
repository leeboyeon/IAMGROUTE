package com.ssafy.groute.src.service

import android.util.Log
import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "PlaceService"
class PlaceService {
    fun getPlaces(callback: RetrofitCallback<List<Places>>){
        val placeRequest : Call<List<Places>> = RetrofitUtil.placeService.listPlace()
        placeRequest.enqueue(object : Callback<List<Places>> {
            override fun onResponse(call: Call<List<Places>>, response: Response<List<Places>>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Places>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun getPlace(placeId: Int, callback: RetrofitCallback<Places>){
        val placeRequest : Call<Places> = RetrofitUtil.placeService.getPlace(placeId)
        placeRequest.enqueue(object : Callback<Places>{
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        Log.d(TAG, "onResponse: ")
                        callback.onSuccess(response.code(), res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
    
    fun updatePlace(place: Places, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.placeService.updatePlace(place).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    Log.d(TAG, "onResponse: UpdateSuccess!!")
                }else{
                    Log.d(TAG, "onResponse: FAIL")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.d(TAG, "onFailure: $t")
            }

        })
    }
}