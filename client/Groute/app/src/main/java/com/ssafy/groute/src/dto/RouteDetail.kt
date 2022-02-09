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
    constructor(id:Int,memo:String,priority: Int):this(id,memo,Place(),0,priority,0)
    constructor():this(0,"",Place(),0,0,0)
    constructor(id:Int, placeId: Int,priority: Int,routeId: Int): this(id,"",Place(),placeId,priority,routeId)
    constructor(id:Int, priority: Int):this(id,"",Place(),0,priority,0)
}