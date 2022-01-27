package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.RouteDetail
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteDetailService {

    fun deleteRouteDetail(id:Int, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.routeDetailService.deleteRouteDetail(id).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun getRouteDetailbyId(id:Int, callback:RetrofitCallback<RouteDetail>){
        RetrofitUtil.routeDetailService.getRouteDetailbyId(id).enqueue(object : Callback<RouteDetail> {
            override fun onResponse(call: Call<RouteDetail>, response: Response<RouteDetail>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<RouteDetail>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun insertRouteDetail(routeDetail:RouteDetail , callback: RetrofitCallback<Boolean>) {
        RetrofitUtil.routeDetailService.insertRouteDetail(routeDetail).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun getRouteDetailList(callback: RetrofitCallback<MutableList<RouteDetail>>){
        RetrofitUtil.routeDetailService.getRouteDetailList().enqueue(object : Callback<MutableList<RouteDetail>> {
            override fun onResponse(
                call: Call<MutableList<RouteDetail>>,
                response: Response<MutableList<RouteDetail>>
            ) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<MutableList<RouteDetail>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun updateRouteDetail(routeDetail:RouteDetail, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.routeDetailService.updateRouteDetail(routeDetail).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
}