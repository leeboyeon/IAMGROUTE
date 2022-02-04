package com.ssafy.groute.src.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_FREE_TYPE
import com.ssafy.groute.src.main.board.BoardFragment.Companion.BOARD_QUESTION_TYPE
import com.ssafy.groute.src.main.board.CommentAdapter
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import com.ssafy.groute.src.service.BoardService
import kotlinx.coroutines.launch


private const val TAG = "BoardViewModel_groute"
class BoardViewModel : ViewModel(){
    var _boardFreeList = BoardService().getBoardDetailList(1)
    val boardFreeList: MutableLiveData<MutableList<BoardDetail>>
        get() = _boardFreeList

    var _boardQuestionList =  BoardService().getBoardDetailList(2)
    val boardQuestionList : MutableLiveData<MutableList<BoardDetail>>
        get() = _boardQuestionList

    val _commentCount : MutableLiveData<Int> = MutableLiveData()
    val _commentList : MutableLiveData<List<Comment>> = MutableLiveData()
    var _commentNestedCount : MutableLiveData<Int> = MutableLiveData()
    var _commentNestedList : MutableLiveData<List<Comment>> = MutableLiveData()

    val commentCount : LiveData<Int>
        get() = _commentCount
    val commentList: LiveData<List<Comment>>
        get() = _commentList

    val commentNestedCount : LiveData<Int>
        get() = _commentNestedCount
    val commentNestedList: LiveData<List<Comment>>
        get() = _commentNestedList


    fun setFreeList(freeList: MutableList<BoardDetail>) = viewModelScope.launch{
        _boardFreeList.value = freeList
        _boardFreeList.postValue(freeList)
    }

    fun setQuestionList(questionList: MutableList<BoardDetail>) = viewModelScope.launch {
        _boardQuestionList.value = questionList
        _boardQuestionList.postValue(questionList)
    }

    fun setBoardDetailWithComment(data: BoardDetailWithCommentResponse) = viewModelScope.launch {
        var list = mutableListOf<Comment>()
        for(i in 0 until data.commentList.size) {
            if(data.commentList.get(i).level == 0) {
                list.add(data.commentList.get(i))
            }
        }
        _commentCount.value = list.size
        _commentList.value = list
    }

    fun setCommentNested(data: BoardDetailWithCommentResponse, groupNum: Int) = viewModelScope.launch {
        var list = mutableListOf<Comment>()
        for(i in 0 until data.commentList.size) {
            if(data.commentList.get(i).groupNum == groupNum) {
                if(data.commentList.get(i).level == 1) {
                    list.add(data.commentList.get(i))
                }
            }
        }
        _commentNestedList.value = list
        _commentNestedCount.value = list.size
        Log.d(TAG, "setCommentNested: $list")
        //_commentNestedList.value = data.commentList

    }

    fun getBoardFreeListFive(owner:LifecycleOwner) {
        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardFreeList: $it")
            val tmpList: ArrayList<BoardDetail> = arrayListOf()
            if(!it.isEmpty()) {
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
            if(!it.isEmpty()) {
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

    fun getBoardDetailWithNestedComment(owner: LifecycleOwner, boardDetailId: Int, groupNum: Int) {
        BoardService().getBoardDetailWithComment(boardDetailId).observe(owner, Observer {
            Log.d(TAG, "getBoardDetailWithNestedComment observe: $it")
            setCommentNested(it, groupNum)
        })
    }


    fun getBoardFreeList(owner:LifecycleOwner) {
        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardFreeList: $it")
            setFreeList(it)
        })
    }
    fun getBoardQuestionList(owner: LifecycleOwner){
        BoardService().getBoardDetailList(BOARD_QUESTION_TYPE).observe(owner, Observer {
            Log.d(TAG, "getBoardQuestionList: $it")
            setQuestionList(it)
        })
    }
}