package com.ssafy.groute.src.dto

data class Account(
    val cateImg:String,
    val category: String,
    val description: String,
    val id: Int,
    val day:Int,
    val userPlanId: Int,
    val spentMoney: Int,
    val type:String
)