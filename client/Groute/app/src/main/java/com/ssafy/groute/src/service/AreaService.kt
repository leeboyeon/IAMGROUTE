package com.ssafy.groute.src.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ssafy.groute.src.dto.Category
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil

class AreaService {

    fun getAreas(callback:RetrofitCallback<List<Category>>){
        val areaRequest : Call<List<Category>> = RetrofitUtil.areaService.listArea()
        areaRequest.enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                val res = response.body()
                if(response.code() == 200){
                    if(res!=null){
                        callback.onSuccess(response.code(), res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                callback.onError(t)
            }

        })
    }
}


