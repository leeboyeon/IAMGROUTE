package com.ssafy.groute.src.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "CommentService_groute"
class CommentService {

    fun insertBoardComment(comment: Comment, callback: RetrofitCallback<Any>){
        RetrofitUtil.commentService.insertBoardComment(comment).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        callback.onSuccess(response.code(), res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun updateBoardComment(comment: Comment, callback: RetrofitCallback<Any>){
        RetrofitUtil.commentService.updateBoardComment(comment).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback.onError(t)
            }

        })
    }

    fun deleteBoardComment(id: Int, callback: RetrofitCallback<Any>) {
        RetrofitUtil.commentService.deleteBoardComment(id).enqueue(object : Callback<Any>{
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                val res = response.body()
                if(response.code() == 200) {
                    if(res != null) {
                        callback.onSuccess(response.code(), res)
                    }
                } else {
                    callback.onFailure(response.code())
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun selectBoardComment(id: Int): LiveData<Comment> {
        val responseLiveData: MutableLiveData<Comment> = MutableLiveData()
        val boardCommentRequest: Call<Comment> = RetrofitUtil.commentService.selectBoardComment(id)
        Log.d(TAG, "getBoardDetailWithComment: $id")
        boardCommentRequest.enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                val res = response.body()
                if(response.code() == 200){
                    if (res != null) {
                        responseLiveData.postValue(res)
                        Log.d(TAG, "onResponse: $res")
                    }
                } else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.d(TAG, t.message ?: "통신오류")
            }
        })
        return responseLiveData
    }
}