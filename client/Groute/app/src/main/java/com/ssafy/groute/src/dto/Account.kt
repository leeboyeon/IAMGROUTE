package com.ssafy.groute.src.dto

data class Account(
    val categoryId:Int,
    val categoryName: String,
    val day:Int,
    val description: String,
    val id: Int,
    val img:String,
    val userPlanId: Int,
    val spentMoney: Int,
    val type:String
){
    constructor(categoryId: Int,day: Int,description: String,userPlanId: Int,spentMoney: Int,type: String):this(categoryId,"",day, description, 0, "", userPlanId, spentMoney, type)
}