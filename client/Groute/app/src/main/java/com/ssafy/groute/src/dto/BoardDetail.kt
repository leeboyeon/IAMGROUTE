package com.ssafy.groute.src.dto

data class BoardDetail(
    val id: Int,
    val title: String,
    val content: String,
    val img: String,
    val createDate: String,
    val updateDate: String,
    val heartCnt: Int,
    val hitCnt: Int,
    val boardId: Int,
    val userId: String
)
