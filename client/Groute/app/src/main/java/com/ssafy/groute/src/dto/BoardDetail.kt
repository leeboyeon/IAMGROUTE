package com.ssafy.groute.src.dto

data class BoardDetail(
    val id: Int,
    var title: String,
    var content: String,
    var img: String,
    val createDate: String,
    val updateDate: String,
    var heartCnt: Int,
    val hitCnt: Int,
    val boardId: Int,
    val userId: String,
    val placeId: Int,
    val commentCnt : Int
){
    constructor(title:String,content:String,img:String, boardId:Int, userId:String):this(0,title,content,img,"","",0,0, boardId,userId,0,0)
    constructor(id:Int,title:String,content:String,img:String, boardId:Int, userId:String): this(id,title,content,img,"","",0,0, boardId,userId,0,0)
    constructor(title:String,content:String,img:String, boardId:Int, userId:String, placeId:Int):this(0,title,content,img,"","",0,0, boardId,userId,placeId,0)
    constructor(id:Int, title:String,content:String,img:String, boardId:Int, userId:String, placeId:Int):this(id,title,content,img,"","",0,0, boardId,userId,placeId,0)
    constructor(title:String,content:String,img:String, boardId:Int, heartCnt: Int, userId:String):this(0,title,content,img,"","",heartCnt,0, boardId,userId,0,0)
}

