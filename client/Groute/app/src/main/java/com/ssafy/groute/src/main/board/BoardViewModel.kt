package com.ssafy.groute.src.main.board

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.service.BoardService
import kotlinx.coroutines.launch


private const val TAG = "BoardViewModel_groute"
class BoardViewModel : ViewModel(){
    val _boardFreeList =  MutableLiveData<List<BoardDetail>>()
    val boardFreeList: LiveData<List<BoardDetail>>
        get() = _boardFreeList

    val _boardQuestionList =  MutableLiveData<List<BoardDetail>>()
    val boardQuestionList : LiveData<List<BoardDetail>>
        get() = _boardQuestionList

    fun setFreeList(freeList: List<BoardDetail>) = viewModelScope.launch{
        _boardFreeList.value = freeList
        _boardFreeList.postValue(freeList)
    }

    fun setQuestionList(questionList: List<BoardDetail>) = viewModelScope.launch {
        _boardQuestionList.value = questionList
        _boardQuestionList.postValue(questionList)
    }

    fun getBoardFreeList(owner: LifecycleOwner) {
        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardFreeList: $it")
            setFreeList(it)
        })
    }

    fun getBoardQuestionList(owner: LifecycleOwner) {
        BoardService().getBoardDetailList(BOARD_QUESTION_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardQuestionList: $it")
            setQuestionList(it)
        })
    }

}