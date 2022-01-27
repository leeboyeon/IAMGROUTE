package com.ssafy.groute.src.dto

data class Account(
    val category: String,
    val description: String,
    val id: Int,
    val routesId: Int,
    val spentMoney: Int
)