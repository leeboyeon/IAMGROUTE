package com.ssafy.groute.src.dto

data class PlanReview(
    val content: String,
    val createDate: String,
    val id: Int,
    val img: String,
    val planId: Int,
    val rate: Double,
    val title: String,
    val userId: String
){
    constructor(planId: Int,userId: String,content: String,rate: Double,img: String) : this(content,"",0,img,planId, rate,"",userId)
    constructor(planId: Int,userId: String,content: String,rate: Double,img: String,id:Int): this(content,"",id,img,planId,rate,"",userId)
}