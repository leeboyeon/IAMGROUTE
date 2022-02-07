package com.ssafy.groute.src.dto

data class RouteDetail(
    val id: Int,
    val memo: String,
    val place: Place,
    val placeId: Int,
    val priority: Int,
    val routeId: Int
){
    constructor(placeId: Int,priority: Int,routeId: Int): this(0,"",Place(),placeId,priority,routeId)
}