package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.Places
import com.ssafy.groute.src.main.home.Place
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}