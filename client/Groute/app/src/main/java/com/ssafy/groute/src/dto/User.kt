package com.ssafy.groute.src.dto

import java.sql.Date
import java.sql.Timestamp

data class User(
    val id: String,
    val password: String,
    val nickname: String,
    val phone: String,
    val gender: String,
    val birth: String,
    val email: String,
    val type: String,
    val token: String,
    val img: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "")
    constructor(id: String, password: String) : this(id, password, "", "", "", "", "", "", "", "")
    constructor(id: String) : this(id, "", "", "", "", "", "", "", "", "")
}
