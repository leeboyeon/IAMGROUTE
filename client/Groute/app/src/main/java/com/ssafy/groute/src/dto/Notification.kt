package com.ssafy.groute.src.dto

data class Notification(
    val id: Int,
    val userId: String,
    val category: String,
    val content: String,
    val date: String
) {
    constructor(userId: String, category: String, content: String): this(0, userId, category, content, "")    // insert
    constructor(id: Int, userId: String, category: String, content: String): this(id, userId, category, content, "")  // update
}