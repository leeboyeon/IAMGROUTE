package com.ssafy.groute.util

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.service.BoardService
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "BoardViewModel_groute"
class BoardViewModel : ViewModel(){
    var boardFreeList =  MutableLiveData<LiveData<MutableList<BoardDetail>>>()
    val boardQuestionList =  MutableLiveData<List<BoardDetail>>()
    val errorMessage = MutableLiveData<String>()
    var freeList =  BoardService().getBoardDetailList(1)
    var questionList = BoardService().getBoardDetailList(2)


    private var questionBoards = MutableLiveData<LiveData<MutableList<BoardDetail>>>().apply {
        value = questionList
    }
    var freeBoards = MutableLiveData<LiveData<MutableList<BoardDetail>>>().apply {
        value = freeList
    }
    fun getFreelist() : LiveData<MutableList<BoardDetail>>{
        return freeList
    }
    fun getQuestionlist() : LiveData<MutableList<BoardDetail>>{
        return questionList
    }

    val freeLiveData : LiveData<MutableList<BoardDetail>> get() = freeList
    fun loadData() = viewModelScope.launch {
        freeBoards.value = BoardService().getBoardDetailList(1)
        freeBoards.postValue(BoardService().getBoardDetailList(1))
    }

}