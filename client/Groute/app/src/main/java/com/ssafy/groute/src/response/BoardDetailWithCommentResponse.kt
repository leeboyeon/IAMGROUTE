package com.ssafy.groute.src.response

import com.google.gson.annotations.SerializedName
import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import java.io.Serializable

data class BoardDetailWithCommentResponse(
    @SerializedName("boardDetail") var boardDetail: BoardDetail,
    @SerializedName("comments") var comment: Comment,
) : Serializable
