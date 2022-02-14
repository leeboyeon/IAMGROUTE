package com.ssafy.groute.src.dto

data class Route(
    val day: Int,
    val id: Int,
    val isCustom: String,
    val memo: String,
    val name: String,
    var routeDetailList: List<RouteDetail>
)  {
    constructor():this(0,0,"","","", listOf())

}