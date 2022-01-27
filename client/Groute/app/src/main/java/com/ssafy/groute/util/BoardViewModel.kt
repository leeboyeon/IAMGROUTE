package com.ssafy.groute.util

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import com.ssafy.groute.src.service.BoardService
import kotlinx.coroutines.launch


private const val TAG = "BoardViewModel_groute"
class BoardViewModel : ViewModel(){
    val _boardFreeList = BoardService().getBoardDetailList(1)
    val boardFreeList: MutableLiveData<MutableList<BoardDetail>>
        get() = _boardFreeList

    val _boardQuestionList =  BoardService().getBoardDetailList(2)
    val boardQuestionList : MutableLiveData<MutableList<BoardDetail>>
        get() = _boardQuestionList

    val _commentCount : MutableLiveData<Int> = MutableLiveData()
    val _commentList : MutableLiveData<List<Comment>> = MutableLiveData()

    val commentCount : LiveData<Int>
        get() = _commentCount
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    fun setFreeList(freeList: MutableList<BoardDetail>) = viewModelScope.launch{
        _boardFreeList.value = freeList
        _boardFreeList.postValue(freeList)
    }

    fun setQuestionList(questionList: MutableList<BoardDetail>) = viewModelScope.launch {
        _boardQuestionList.value = questionList
        _boardQuestionList.postValue(questionList)
    }

    fun setBoardDetailWithComment(data: BoardDetailWithCommentResponse) = viewModelScope.launch {
        _commentCount.value = data.commentList.size
        _commentList.value = data.commentList
    }

    fun getBoardFreeListFive(owner:LifecycleOwner) {
        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardFreeList: $it")
            val tmpList: ArrayList<BoardDetail> = arrayListOf()
            if(it != null) {
                for (i in 0..4) {
                    tmpList.add(it.get(i))
                }
            }
            setFreeList(tmpList)
        })
    }
    fun getBoardQuestionListFive(owner: LifecycleOwner){
        BoardService().getBoardDetailList(BOARD_QUESTION_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardQuestionList: $it")
            val tmpList: ArrayList<BoardDetail> = arrayListOf()
            if(it != null) {
                for (i in 0..4) {
                    tmpList.add(it.get(i))
                }
            }
            setQuestionList(tmpList)
        })
    }

    fun getBoardDetailWithComment(owner: LifecycleOwner, boardDetailId: Int) {
        BoardService().getBoardDetailWithComment(boardDetailId).observe(owner, Observer {
            Log.d(TAG, "getBoardDetailWithComment observe: $it")
            setBoardDetailWithComment(it)
        })
    }
}