package com.ssafy.groute.src.dto

data class Route(
    val day: Int,
    val id: Int,
    val isCustom: String,
    val memo: String,
    val name: String,
    val routeDetailList: List<RouteDetail>
)