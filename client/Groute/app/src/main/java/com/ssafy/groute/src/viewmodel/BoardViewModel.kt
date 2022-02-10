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
//    var _boardFreeList = BoardService().getBoardDetailList(1)
//    val boardFreeList: MutableLiveData<MutableList<BoardDetail>>
//        get() = _boardFreeList
//
//    var _boardQuestionList =  BoardService().getBoardDetailList(2)
//    val boardQuestionList : MutableLiveData<MutableList<BoardDetail>>
//        get() = _boardQuestionList

//    val _commentCount : MutableLiveData<Int> = MutableLiveData()
//    val _commentList : MutableLiveData<List<Comment>> = MutableLiveData()
//    var _commentNestedCount : MutableLiveData<Int> = MutableLiveData()
//    var _commentNestedList : MutableLiveData<List<Comment>> = MutableLiveData()
//
//    val commentCount : LiveData<Int>
//        get() = _commentCount
//
//    val commentList: LiveData<List<Comment>>
//        get() = _commentList
//
//    val commentNestedCount : LiveData<Int>
//        get() = _commentNestedCount
//    val commentNestedList: LiveData<List<Comment>>
//        get() = _commentNestedList


//    fun setFreeList(freeList: MutableList<BoardDetail>) = viewModelScope.launch{
//        _boardFreeList.value = freeList
//        _boardFreeList.postValue(freeList)
//    }
//
//    fun setQuestionList(questionList: MutableList<BoardDetail>) = viewModelScope.launch {
//        _boardQuestionList.value = questionList
//        _boardQuestionList.postValue(questionList)
//    }

//    fun getBoardFreeListFive(owner:LifecycleOwner) {
//        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
//            Log.d(TAG, "getBoardFreeList: $it")
//            val tmpList: ArrayList<BoardDetail> = arrayListOf()
//            if(!it.isEmpty()) {
//                if(it.size >= 5) {
//                    for (i in 0..4) {
//                        tmpList.add(it.get(i))
//                    }
//                    setFreeList(tmpList)
//                } else {
//                    setFreeList(it)
//                }
//            }
//        })
//    }
//
//    fun getBoardQuestionListFive(owner: LifecycleOwner){
//        BoardService().getBoardDetailList(BOARD_QUESTION_TYPE).observe(owner, Observer {
//            Log.d(TAG, "getBoardQuestionList: $it")
//            val tmpList: ArrayList<BoardDetail> = arrayListOf()
//            if(!it.isEmpty()) {
//                if(it.size >= 5) {
//                    for (i in 0..4) {
//                        tmpList.add(it.get(i))
//                    }
//                    setQuestionList(tmpList)
//                } else {
//                    setQuestionList(it)
//                }
//            }
//
//        })
//    }

//    fun getBoardDetailWithComment(owner: LifecycleOwner, boardDetailId: Int) {
//        BoardService().getBoardDetailWithComment(boardDetailId).observe(owner, Observer {
//            Log.d(TAG, "getBoardDetailWithComment observe: $it")
//            setBoardDetailWithComment(it)
//        })
//    }
//
//    fun getBoardDetailWithNestedComment(owner: LifecycleOwner, boardDetailId: Int, groupNum: Int) {
//        BoardService().getBoardDetailWithComment(boardDetailId).observe(owner, Observer {
//            Log.d(TAG, "getBoardDetailWithNestedComment observe: $it")
//            setCommentNested(it, groupNum)
//        })
//    }


//    fun getBoardFreeList(owner:LifecycleOwner) {
//        BoardService().getBoardDetailList(BOARD_FREE_TYPE).observe(owner, Observer {
//            Log.d(TAG, "getBoardFreeList: $it")
//            setFreeList(it)
//        })
//    }
//    fun getBoardQuestionList(owner: LifecycleOwner){
//        BoardService().getBoardDetailList(BOARD_QUESTION_TYPE).observe(owner, Observer {
//            Log.d(TAG, "getBoardQuestionList: $it")
//            setQuestionList(it)
//        })
//    }


    // 게시판 id에 해당하는 게시글 리스트 조회
    private val _freeBoardPostList = MutableLiveData<MutableList<BoardDetail>>()
    private val _qnaBoardPostList = MutableLiveData<MutableList<BoardDetail>>()

    val freeBoardPostList : LiveData<MutableList<BoardDetail>>
        get() = _freeBoardPostList

    val qnaBoardPostList : LiveData<MutableList<BoardDetail>>
        get() = _qnaBoardPostList

    fun setFreeBoardPostList(list : MutableList<BoardDetail>) = viewModelScope.launch {
//        val tmp = mutableListOf<BoardDetail>()
//        if (list.size >= 5) {
//            for (i in 0 until 5) {
//                tmp.add(list[i])
//            }
//            _freeBoardPostList.value = tmp
//        } else {
//            _freeBoardPostList.value = list
//        }
        _freeBoardPostList.value = list
    }

    fun setQnABoardPostList(list : MutableList<BoardDetail>) = viewModelScope.launch {
//        val tmp = mutableListOf<BoardDetail>()
//        if (list.size >= 5) {
//            for (i in 0 until 5) {
//                tmp.add(list[i])
//            }
//            _qnaBoardPostList.value = tmp
//        } else {
//            _qnaBoardPostList.value = list
//        }
        _qnaBoardPostList.value = list
    }

    suspend fun getBoardPostList(boardId : Int) {
        val response = BoardService().getBoardPostList(boardId)
        viewModelScope.launch {
            val res = response.body()
            if (response.code() == 200) {
                if (res != null) {
                    if (boardId == 1) {
                        setFreeBoardPostList(res as MutableList<BoardDetail>)
                    } else if (boardId == 2) {
                        setQnABoardPostList(res as MutableList<BoardDetail>)
                    }
                    Log.d(TAG, "getBoardPostList: ${response.message()}")
                } else {
                    Log.d(TAG, "getBoardPostError: ${response.message()}")
                }
            }
        }
    }

    // 게시글 id에 해당하는 게시글과 댓글 리스트 조회
    private val _boardDetailResponse = MutableLiveData<BoardDetail>()
    private val _commentAllList = MutableLiveData<MutableList<Comment>>()
    private val _commentListResponse = MutableLiveData<MutableList<Comment>>()   // 댓글 level이 0인 comment List
    private val _commentNestedListResponse = MutableLiveData<MutableList<Comment>>()   // 댓글 같은 그룹인 comment List

    val boardDetail : LiveData<BoardDetail>
        get() = _boardDetailResponse

    val commentAllList : LiveData<MutableList<Comment>>
        get() = _commentAllList

    val commentList : LiveData<MutableList<Comment>>
        get() = _commentListResponse

    val commentNestedList : LiveData<MutableList<Comment>>
        get() = _commentNestedListResponse

    fun setBoardDetail(boardDetail: BoardDetail) = viewModelScope.launch {
        _boardDetailResponse.value = boardDetail
    }

    fun setCommentAllList(commentList: MutableList<Comment>) = viewModelScope.launch {
        _commentAllList.value = commentList
    }

    // level이 0인 comment
    fun setCommentList(commentList : MutableList<Comment>) = viewModelScope.launch {
//        _boardDetailWithCmtListResponse.value = commentList
        val list = mutableListOf<Comment>()
        for(i in 0 until commentList.size) {
            if(commentList[i].level == 0) {
                list.add(commentList[i])
            }
        }
        _commentListResponse.value = list
    }

    // 같은 그룹에 level 1인 commentList
    fun setCommentNestedList(commentList: MutableList<Comment>, groupNum: Int) = viewModelScope.launch {
        val list = mutableListOf<Comment>()
        for(i in 0 until commentList.size) {
            if(commentList[i].groupNum == groupNum) {
                if(commentList[i].level == 1) {
                    list.add(commentList[i])
                }
            }
        }
        _commentNestedListResponse.value = list
    }

//    fun setBoardDetailWithComment(data: BoardDetailWithCommentResponse) = viewModelScope.launch {
//        var list = mutableListOf<Comment>()
//        for(i in 0 until data.commentList.size) {
//            if(data.commentList.get(i).level == 0) {
//                list.add(data.commentList.get(i))
//            }
//        }
//        _commentCount.value = list.size
//        _commentList.value = list
//    }



    suspend fun getBoardDetail(boardDetailId : Int) {
        val response = BoardService().getBoardDetailWithCmt(boardDetailId)
        viewModelScope.launch {
            val res = response.body()
            if(response.code() == 200) {
                if(res != null) {
                    setBoardDetail(res.boardDetail)
                    setCommentAllList(res.commentList as MutableList<Comment>)
                    setCommentList(res.commentList as MutableList<Comment>)
                    Log.d(TAG, "getBoardDetailSuccess: ${response.message()}")
                } else {
                    Log.d(TAG, "getBoardDetailError: ${response.message()}")
                }
            }
        }
    }

//    suspend fun getPostCmtList(boardDetailId: Int) {
//        val response = BoardService().getPostCommentList(boardDetailId)
//        viewModelScope.launch {
//            val res = response.body()
//            if(response.code() == 200) {
//                if(res != null) {
////                    setBoardDetail(res.boardDetail)
//                    setCommentAllList(res as MutableList<Comment>)
//                    setCommentList(res as MutableList<Comment>)
//                    Log.d(TAG, "getPostCmtListSuccess: ${response.message()}")
//                } else {
//                    Log.d(TAG, "getPostCmtListError: ${response.message()}")
//                }
//            }
//        }
//    }

    private val _isBoardPostLike = MutableLiveData<Boolean>()

    val isBoardPostLike : LiveData<Boolean>
        get() = _isBoardPostLike

    fun setIsBoardPostLike(res : Boolean) = viewModelScope.launch {
        _isBoardPostLike.value = res
    }


    suspend fun getBoardPostIsLike(boardDetailId: Int, userId : String) {
        val response = BoardService().getBoardPostIsLike(boardDetailId, userId)
        viewModelScope.launch {
            val res = response.body()
            if(response.code() == 200) {
                if(res != null) {
                    setIsBoardPostLike(res)
                    Log.d(TAG, "getBoardPostIsLikeSuccess: ${response.message()}")
                } else {
                    Log.d(TAG, "getBoardPostIsLikeError: ${response.message()}")
                }
            }
        }
    }


}