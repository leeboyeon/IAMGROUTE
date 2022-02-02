package com.ssafy.groute.src.service

import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

}