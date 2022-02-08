package com.ssafy.groute.src.dto

data class SharedUser(
    val id: Int,
    val planId: Int,
    val userId: String
){
    constructor(planId: Int,userId: String):this(0,planId,userId)
}