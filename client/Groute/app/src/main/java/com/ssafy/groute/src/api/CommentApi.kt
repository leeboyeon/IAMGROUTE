package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.dto.Comment
import retrofit2.Call
import retrofit2.http.*

interface CommentApi {

    // 게시판 댓글 추가
    @POST("/boardDetail/comment/insert")
    fun insertBoardComment(@Body comment: Comment) : Call<Any>

    // 게시판 댓글 수정
    @PUT("/boardDetail/comment/update")
    fun updateBoardComment(@Body comment: Comment): Call<Any>

    // 게시판 댓글 삭제
    @DELETE("/boardDetail/comment/del")
    fun deleteBoardComment(@Query("id") id: Int) : Call<Any>

    // 게시판 댓글 조회
    @GET("/boardDetail/comment/detail")
    fun selectBoardComment(@Query("id") id: Int) : Call<Comment>

    // 게시판 댓글 조회 - coroutine ver.
    @GET("/boardDetail/comment/detail")
    suspend fun getBoardCommentList(@Query("id") id: Int) : Call<Comment>
}