package com.ssafy.groute.src.dto

data class UserPlan(
    val description: String,
    val endDate: String,
    val heartCnt: Int,
    val id: Int,
    val isPublic: String,
    val rate: Int,
    val startDate: String,
    val title: String,
    val totalDate: Int,
    val userId: String
)