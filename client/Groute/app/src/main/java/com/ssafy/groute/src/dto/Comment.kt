package com.ssafy.groute.src.dto

data class Comment(
    val id: Int,
    val boardDetailId: Int,
    var content: String,
    val groupNum: Int,
    val level: Int,
    val order: Int,
    val userId: String
) {
    constructor(boardDetailId: Int, content: String, groupNum: Int, userId: String): this(0, boardDetailId, content, groupNum, 0, 0, userId)
}
