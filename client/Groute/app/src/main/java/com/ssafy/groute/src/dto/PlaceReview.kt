package com.ssafy.groute.src.dto

data class PlaceReview(
    val content: String,
    val createDate: String,
    val id: Int,
    val img: String,
    val placeId: Int,
    val rate: Int,
    val title: String,
    val userId: String
)