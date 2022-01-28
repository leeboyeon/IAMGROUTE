package com.ssafy.groute.src.dto

data class BoardDetail(
    val id: Int,
    var title: String,
    var content: String,
    val img: String,
    val createDate: String,
    val updateDate: String,
    val heartCnt: Int,
    val hitCnt: Int,
    val boardId: Int,
    val userId: String,
    val placeId: Int
){
    constructor(title:String,content:String,img:String, boardId:Int, userId:String):this(0,title,content,img,"","",0,0, boardId,userId,0)
    constructor(id:Int,title:String,content:String,img:String, boardId:Int, userId:String): this(id,title,content,img,"","",0,0, boardId,userId,0)
    constructor(title:String,content:String,img:String, boardId:Int, userId:String, placeId:Int):this(0,title,content,img,"","",0,0, boardId,userId,placeId)
}

