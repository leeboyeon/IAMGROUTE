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
){
    constructor(title:String,content:String,img:String, boardId:Int, userId:String):this(0,title,content,img,"","",0,0, boardId,userId)
    constructor(title:String,content:String,img:String, boardId:Int, userId:String, id:Int): this(id,title,content,img,"","",0,0, boardId,userId)
}

