package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.Route
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteService {
    /*
    * ID로 route 가져오기
    * */
    fun getRoutebyId(routeId:Int, callback: RetrofitCallback<MutableList<Route>>){
        RetrofitUtil.routeService.getRoutebyId(routeId).enqueue(object : Callback<MutableList<Route>> {
            override fun onResponse(
                call: Call<MutableList<Route>>,
                response: Response<MutableList<Route>>
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

            override fun onFailure(call: Call<MutableList<Route>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
    /*
    * route 추가하기
    * */
    fun insertRoute(route:Route, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.routeService.insertRoute(route).enqueue(object:Callback<Boolean> {
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
    /*
    * 모든 route List 가져오기
    * */
    fun getRouteList(callback:RetrofitCallback<MutableList<Route>>){
        RetrofitUtil.routeService.getRouteList().enqueue(object : Callback<MutableList<Route>> {
            override fun onResponse(
                call: Call<MutableList<Route>>,
                response: Response<MutableList<Route>>
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

            override fun onFailure(call: Call<MutableList<Route>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    /*
    *route 삭제하기
    * */
    fun deleteRoute(routeId:Int, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.routeService.deleteRoute(routeId).enqueue(object : Callback<Boolean> {
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

    /*
    * route 수정*
    */
    fun updateRoute(route:Route, callback:RetrofitCallback<Boolean>){
        RetrofitUtil.routeService.updateRoute(route).enqueue(object : Callback<Boolean> {
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