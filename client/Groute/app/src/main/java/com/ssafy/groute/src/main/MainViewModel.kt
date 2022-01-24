package com.ssafy.groute.src.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.groute.src.dto.BoardDetail

class MainViewModel : ViewModel() {
    val boardDetailList = mutableListOf<BoardDetail>()
    val liveboardDetailList = MutableLiveData<MutableList<BoardDetail>>().apply {
        value = boardDetailList
    }

    fun removeBoardDetailItem(position: Int){
        boardDetailList.removeAt(position)
        liveboardDetailList.value = boardDetailList
    }
    fun insertBoardDetailItem(boardDetail: BoardDetail){
        boardDetailList.add(boardDetail)
        liveboardDetailList.value = boardDetailList
    }
}