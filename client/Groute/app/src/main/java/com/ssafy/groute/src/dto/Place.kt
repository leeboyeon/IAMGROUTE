package com.ssafy.groute.src.dto

data class Place(
    val address: String,
    val areaId: Int,
    val contact: String,
    val description: String,
    val heartCnt: Int,
    val id: Int,
    val img: String,
    val lat: String,
    val lng: String,
    val name: String,
    val rate: Float,
    val themeId: Int,
    val type: String,
    val userId: String,
    val zipCode: String
){
    constructor():this("",0,"","",0,0,"","","","",0F,0,"","","")
}