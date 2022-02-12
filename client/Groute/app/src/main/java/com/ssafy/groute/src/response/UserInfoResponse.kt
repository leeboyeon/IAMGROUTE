package com.ssafy.groute.src.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserInfoResponse(
    @SerializedName("id") var id: String,
    @SerializedName("password") var password: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("gender") var gender: String?,
    @SerializedName("birth") var birth: String,
    @SerializedName("email") var email: String,
    @SerializedName("type") var type: String,
    @SerializedName("token") var token: String?,
    @SerializedName("img") var img: String?,
    @SerializedName("createDate") var createDate: String?,
    @SerializedName("updateDate") var updateDate: String?,
    @SerializedName("username") var username: String?,
    @SerializedName("enabled") var enabled: Boolean,
    @SerializedName("authorities") var authorities: String?,
    @SerializedName("credentialsNonExpired") var credentialsNonExpired: Boolean,
    @SerializedName("accountNonLocked") var accountNonLocked: Boolean,
    @SerializedName("accountNonExpired") var accountNonExpired: Boolean
) : Serializable
{
    constructor(id: String, password: String): this(id, password, "", "", "", "", "", "", null, null, null, null, null, false, null, false, false, false)
}
