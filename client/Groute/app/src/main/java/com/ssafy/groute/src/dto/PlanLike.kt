package com.ssafy.groute.src.dto

data class PlanLike(
    val id: Int,
    val userId: String,
    val userPlanId: Int
) {
    constructor(userId: String, userPlanId: Int) : this(0, userId, userPlanId)
}
