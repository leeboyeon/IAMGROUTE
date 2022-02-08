package com.ssafy.groute.src.dto

import java.sql.Date
import java.sql.Timestamp

data class User(
    val id: String,
    val password: String,
    val nickname: String,
    val phone: String,
    val email: String,
    val birth: String,
    val gender: String?,
    val type: String,
    val token: String,
    val img: String
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "")
    constructor(id: String, password: String) : this(id, password, "", "", "", "", "", "", "", "")
    constructor(id: String, password: String, nickname: String, phone:String, email: String, birth: String, gender: String?, type: String) : this(id, password, nickname, phone, email, birth, gender, type, "", "")
    constructor(id: String, password: String, nickname: String, phone:String, email: String, birth: String, gender: String, type: String, img: String) : this(id, password, nickname, phone, email, birth, gender, type, "", img)
    constructor(id: String) : this(id, "", "", "", "", "", "", "", "", "")
    constructor(id: String, password: String, nickname: String, phone: String, img: String): this(id, password, nickname,phone, "", "", "", "", "", img)
    constructor(id: String,nickname: String,img: String):this(id, "", nickname, "", "", "", "", "", "", img)
}
