package com.ssafy.groute.src.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BoardService_groute"
class BoardService {

    fun getBoardList(): LiveData<List<BoardDetail>> {
        val responseLiveData: MutableLiveData<List<BoardDetail>> = MutableLiveData()
        val boardListRequest: Call<MutableList<BoardDetail>> = RetrofitUtil.boardService.listBoard()
        boardListRequest.enqueue(object : Callback<MutableList<BoardDetail>> {
            override fun onResponse(call: Call<MutableList<BoardDetail>>, response: Response<MutableList<BoardDetail>>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.value = res
                        Log.d(TAG, "onResponse: $res")
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<BoardDetail>>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })
        return responseLiveData
    }

}