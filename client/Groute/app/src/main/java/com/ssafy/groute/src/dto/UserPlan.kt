package com.ssafy.groute.src.dto

data class UserPlan(
    val areaId: Int,
    val description: String?,
    val endDate: String,
    val heartCnt: Int,
    val id: Int,
    val isPublic: String,
    val rate: Double,
    val startDate: String,
    val themeIdList: List<Int>,
    val title: String,
    val totalDate: Int,
    val userId: String
){
    constructor(areaId:Int, description: String,endDate: String,isPublic: String,startDate: String,title: String,totalDate: Int,userId: String)
            :this(areaId, description, endDate, 0, 0, isPublic, 0.0, startDate,
        listOf(), title, totalDate, userId)

    constructor(id: Int,userId: String,title: String,description: String,startDate: String,endDate: String,totalDate: Int,isPublic: String,rate: Double,heartCnt: Int,areaId: Int, themeIdList: List<Int>)
        :this(areaId, description, endDate, heartCnt, id, isPublic, rate, startDate, themeIdList, title, totalDate, userId)
}