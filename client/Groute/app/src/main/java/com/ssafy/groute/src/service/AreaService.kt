package com.ssafy.groute.src.service
import android.util.Log
import com.nhn.android.idp.common.connection.CommonConnection.cancel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ssafy.groute.src.dto.Area
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import kotlinx.coroutines.*
import retrofit2.HttpException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AreaService {

    fun getAreas(callback: RetrofitCallback<List<Area>>) {
        var responseData: MutableList<Area> = mutableListOf()
//        val scope = CoroutineScope(Job() + Dispatchers.IO)
//        scope.launch {

            val areaRequest : Call<List<Area>> = RetrofitUtil.areaService.listArea()
            areaRequest.enqueue(object : Callback<List<Area>> {
                override fun onResponse(call: Call<List<Area>>, response: Response<List<Area>>) {
                    val res = response.body()
                    if(response.code() == 200){
                        if(res!=null){
                            callback.onSuccess(response.code(), res)
                            Log.d("Home", "onResponse: $res")
//                        responseData = res
                        }
                    }else{
                        callback.onFailure(response.code())
                    }
                }

                override fun onFailure(call: Call<List<Area>>, t: Throwable) {
                    callback.onError(t)
                }
            })

//        }



//        return responseData
    }

    suspend fun getAreaList() = RetrofitUtil.areaService.listAreas()


    suspend fun <T : Any> Call<T?>.await(): T? {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback<T?> {
                override fun onResponse(call: Call<T?>, response: Response<T?>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body())
                    } else {
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: Call<T?>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}


