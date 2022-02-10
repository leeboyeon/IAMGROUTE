package com.ssafy.groute.src.dto

data class AccountCategory (
    var id:Int,
    var name:String,
    var img:String
    ){
    constructor(name: String,img: String):this(0,name,img)
}