package com.ssafy.groute.src.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.dto.Place
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import com.ssafy.groute.util.RetrofitCallback
import com.ssafy.groute.util.RetrofitUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BoardService_groute"
class BoardService {

    /**
     * @GET("/boardDetail/list")
     */
//    fun getBoardList(): LiveData<List<BoardDetail>> {
//        val responseLiveData: MutableLiveData<List<BoardDetail>> = MutableLiveData()
//        val boardListRequest: Call<MutableList<BoardDetail>> = RetrofitUtil.boardService.listBoard()
//        boardListRequest.enqueue(object : Callback<MutableList<BoardDetail>> {
//            override fun onResponse(call: Call<MutableList<BoardDetail>>, response: Response<MutableList<BoardDetail>>) {
//                val res = response.body()
//                if(response.code() == 200){
//                    if (res != null) {
//                        responseLiveData.value = res
//                        Log.d(TAG, "onResponse: $res")
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
//                }
//            }
//            override fun onFailure(call: Call<MutableList<BoardDetail>>, t: Throwable) {
//                Log.d(TAG, t.message ?: "통신오류")
//            }
//        })
//        return responseLiveData
//    }


//    fun getBoardDetailList(boardId: Int): MutableLiveData<MutableList<BoardDetail>> {
//        var responseLiveData: MutableLiveData<MutableList<BoardDetail>> = MutableLiveData()
//        val boardDetailListRequest: Call<MutableList<BoardDetail>> = RetrofitUtil.boardService.listBoardDetail(boardId)
//        boardDetailListRequest.enqueue(object : Callback<MutableList<BoardDetail>> {
//            override fun onResponse(call: Call<MutableList<BoardDetail>>, response: Response<MutableList<BoardDetail>>) {
//                var res = response.body()
//                if(response.code() == 200){
//                    if (res != null) {
//                        responseLiveData.postValue(res)
//                        //responseLiveData.value = res
//                        Log.d(TAG, "onResponse: $res")
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<MutableList<BoardDetail>>, t: Throwable) {
//                responseLiveData.postValue(null)
//                Log.d(TAG, t.message ?: "통신오류")
//            }
//        })
//        return responseLiveData
//    }

//    suspend fun getBoardDetailList(boardId: Int): MutableList<BoardDetail> {
//        var list = mutableListOf<BoardDetail>()
//        val boardDetailListRequest: Call<MutableList<BoardDetail>> = RetrofitUtil.boardService.listBoardDetail(boardId)
//        boardDetailListRequest.enqueue(object : Callback<MutableList<BoardDetail>> {
//            override fun onResponse(call: Call<MutableList<BoardDetail>>, response: Response<MutableList<BoardDetail>>) {
//                val res = response.body()
//                if(response.code() == 200){
//                    if (res != null) {
//                        list = res
//                        Log.d(TAG, "onResponse: $res")
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<MutableList<BoardDetail>>, t: Throwable) {
//                Log.d(TAG, t.message ?: "통신오류")
//            }
//        })
//        return list
//    }

    /**
     * 게시판 글 작성, image 업로드는 선택 사항이다.
     * @param board
     * @param img
     * @return boardId
     */
    fun insertBoardDetail(board: RequestBody, img: MultipartBody.Part?, callback: RetrofitCallback<Int>){
        RetrofitUtil.boardService.insertBoardDetail(board, img).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        callback.onSuccess(response.code(), res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    fun deleteBoardDetail(boardDetailId:Int, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.boardService.deleteBoardDetail(boardDetailId).enqueue(object : Callback<Boolean> {
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

    fun boardLike(boardDetailId: Int, userId: String, callback: RetrofitCallback<Any>){
        RetrofitUtil.boardService.likeBoard(boardDetailId, userId).enqueue(object : Callback<Any> {
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

    fun isBoardLike(boardDetailId: Int, userId: String, callback: RetrofitCallback<Boolean>){
        RetrofitUtil.boardService.isLikeBoard(boardDetailId, userId).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        callback.onSuccess(response.code(), res)
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

//    fun getListBoardDetail(id:Int, callback: RetrofitCallback<Map<String,Any>>){
//        RetrofitUtil.boardService.getListBoardDetail(id).enqueue(object : Callback<Map<String,Any>> {
//            override fun onResponse(
//                call: Call<Map<String, Any>>,
//                response: Response<Map<String, Any>>
//            ) {
//                val res = response.body()
//                if(response.code() == 200){
//                    if(res != null){
//                        callback.onSuccess(response.code(), res)
//                    }
//                }else{
//                    callback.onFailure(response.code())
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
//                callback.onError(t)
//            }
//
//
//        })
//    }



    /**
     * 게시글 수정, image upload는 선택 사항이다.
     * @param boardDetail
     * @param img
     * @return boardId
     */
    fun modifyBoardDetail(boardDetail: RequestBody, img: MultipartBody.Part?, callback: RetrofitCallback<Int>){
        RetrofitUtil.boardService.modifyBoardDetail(boardDetail, img).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                val res = response.body()
                if(response.code() == 200){
                    if(res != null){
                        callback.onSuccess(response.code(),res)
                    }
                }else{
                    callback.onFailure(response.code())
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

//    fun getBoardDetailWithComment(id: Int): LiveData<BoardDetailWithCommentResponse> {
//        val responseLiveData: MutableLiveData<BoardDetailWithCommentResponse> = MutableLiveData()
//        val boardDetailWithCommentRequest: Call<BoardDetailWithCommentResponse> = RetrofitUtil.boardService.getBoardDetailWithComment(id)
//        Log.d(TAG, "getBoardDetailWithComment: $id")
//        boardDetailWithCommentRequest.enqueue(object : Callback<BoardDetailWithCommentResponse> {
//            override fun onResponse(call: Call<BoardDetailWithCommentResponse>, response: Response<BoardDetailWithCommentResponse>) {
//                val res = response.body()
//                if(response.code() == 200){
//                    if (res != null) {
//                        responseLiveData.postValue(res)
//                        Log.d(TAG, "onResponse: $res")
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
//                }
//            }
//            override fun onFailure(call: Call<BoardDetailWithCommentResponse>, t: Throwable) {
//                Log.d(TAG, t.message ?: "통신오류")
//            }
//        })
//        return responseLiveData
//    }


    /**
     * 게시판 id에 해당하는 게시글 리스트 조회 - coroutine ver.
     * @param boardId
     * @return Response<List<BoardDetail>>
     */
    suspend fun getBoardPostList(boardId : Int) : Response<List<BoardDetail>> {
        return RetrofitUtil.boardService.getBoardPostList(boardId)
    }


    /**
     * id에 해당하는 게시글과 댓글 조회 - coroutine ver.
     * getBoardDetailWithCmtByCor
     * @param id
     * @return response
     */
    suspend fun getBoardDetailWithCmt(id: Int): Response<BoardDetailWithCommentResponse> {
        return RetrofitUtil.boardService.getBoardDetailWithCmtByCor(id)
    }

    /**
     * 게시글 id에 해당하는 comment List 조회
     * @param boardDetailId
     * @return Response<MutableList<Comment>>
     */
    suspend fun getPostCommentList(boardDetailId: Int) : Response<MutableList<Comment>> {
        return RetrofitUtil.boardService.getPostCommentList(boardDetailId)
    }


    /**
     * id에 해당하는 게시글과 댓글 조회 - coroutine ver.
     * getBoardPostIsLike
     * @param boardDetailId
     * @param userId
     * @return response
     */
    suspend fun getBoardPostIsLike(boardDetailId: Int, userId: String): Response<Boolean> {
        return RetrofitUtil.boardService.isLikeBoardPost(boardDetailId, userId)
    }

    /**
     * id에 해당하는 게시글과 댓글 조회 (조회수 안올라가게) - coroutine ver.
     * getBoardDetailWithCmtNoHit
     * @param id
     * @return response
     */
    suspend fun getBoardDetailWithCmtNoHit(id: Int): Response<BoardDetailWithCommentResponse> {
        return RetrofitUtil.boardService.getBoardDetailWithCmtNoHit(id)
    }



}