package com.ssafy.groute.src.response
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BoardDetailResponse (
    @SerializedName("img") val boardImg:String,
    @SerializedName("boardDetailId") val boardDetailId:Int,
    @SerializedName("boardId") val boardId:Int,
        ): Serializable