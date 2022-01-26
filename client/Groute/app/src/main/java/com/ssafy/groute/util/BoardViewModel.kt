package com.ssafy.groute.util

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.service.BoardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BoardViewModel_groute"
class BoardViewModel : ViewModel(){
    val boardFreeList =  MutableLiveData<MutableList<BoardDetail>>()
    val boardQuestionList =  MutableLiveData<List<BoardDetail>>()
    val errorMessage = MutableLiveData<String>()
    var freeList =  BoardService().getBoardDetailList(1)
    var questionList = BoardService().getBoardDetailList(2)

    private var questionBoards = MutableLiveData<LiveData<MutableList<BoardDetail>>>().apply {
        value = questionList
    }
    private var freeBoards = MutableLiveData<LiveData<MutableList<BoardDetail>>>().apply {
        value = freeList
    }
    fun getFreelist() : LiveData<MutableList<BoardDetail>>{
        return freeList
    }
    fun getQuestionlist() : LiveData<MutableList<BoardDetail>>{
        return questionList
    }
    fun getBoardDetailList(boardId: Int)  {
        //var boardDetailList =  mutableListOf<BoardDetail>()
        val boardDetailListRequest: Call<MutableList<BoardDetail>> = RetrofitUtil.boardService.listBoardDetail(boardId)
        boardDetailListRequest.enqueue(object : Callback<MutableList<BoardDetail>> {
            override fun onResponse(call: Call<MutableList<BoardDetail>>, response: Response<MutableList<BoardDetail>>) {
                val res = response.body()

                if(response.code() == 200) {
                    if(res != null) {
                        Log.d(TAG, "onResponse getBoardDetailList: $res")
                        if(boardId == BOARD_FREE_TYPE) {
                            boardFreeList.postValue(res!!)
                        } else if(boardId == BOARD_QUESTION_TYPE) {
                            boardQuestionList.postValue(res!!)
                        }

                    }
                }else {
                    Log.d(TAG, "onResponse: Error Code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MutableList<BoardDetail>>, t: Throwable) {
                errorMessage.postValue(t.message)
            }

        })
    }

}