package com.ssafy.groute.src.dto

data class PlaceReview(
    val content: String,
    val createDate: String,
    val id: Int,
    var img: String,
    val placeId: Int,
    val rate: Double,
    val title: String,
    val userId: String
){
    constructor(placeId: Int,userId: String,content: String,rate: Double,img: String) : this(content,"",0,img,placeId, rate,"",userId)
    constructor(placeId: Int,userId: String,content: String,rate: Double,img: String,id:Int): this(content,"",id,img,placeId,rate,"",userId)
}