package com.ssafy.groute.src.dto

import java.io.Serializable


data class Comment(
    val id: Int,
    val boardDetailId: Int,
    var content: String,
    val groupNum: Int,
    val level: Int,
    val order: Int,
    val userId: String
) : Serializable
{
    constructor(boardDetailId: Int, content: String, groupNum: Int, userId: String): this(0, boardDetailId, content, groupNum, 0, 0, userId)
    constructor(boardDetailId: Int, content: String, groupNum: Int, userId: String, level: Int, order: Int): this(0, boardDetailId, content, groupNum, level, order, userId)
}
