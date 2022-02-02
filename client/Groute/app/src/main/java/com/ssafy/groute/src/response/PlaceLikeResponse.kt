package com.ssafy.groute.src.response

import java.io.Serializable
import com.google.gson.annotations.SerializedName
data class PlaceLikeResponse (
    @SerializedName("id") var id:Int,
    @SerializedName("userId") var userId:String,
    @SerializedName("placeId") var placeId:Int,
):Serializable