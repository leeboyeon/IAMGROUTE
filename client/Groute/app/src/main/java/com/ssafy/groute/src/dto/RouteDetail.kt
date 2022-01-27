package com.ssafy.groute.src.dto

data class RouteDetail(
    val id: Int,
    val memo: String,
    val place: Places,
    val placeId: Int,
    val priority: Int,
    val routeId: Int
)