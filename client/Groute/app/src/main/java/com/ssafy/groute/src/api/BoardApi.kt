package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import retrofit2.Call
import retrofit2.http.*

interface BoardApi {

    // 자유게시판, 질문게시판 게시글 리스트 조회
    @GET("/boardDetail/list")
    fun listBoard() : Call<MutableList<BoardDetail>>

    // 게시판 타입에 따른 리스트 조회
    @GET("/boardDetail/list/division")
    fun listBoardDetail(@Query("boardId") boardId : Int) : Call<MutableList<BoardDetail>>

    // 게시판에 글쓰기
    @POST("/boardDetail/insert")
    fun insertBoardDetail(@Body boardDetail:BoardDetail) : Call<Boolean>

    // 게시판 글삭제
    @DELETE("/boardDetail/del")
    fun deleteBoardDetail(@Query("id") id: Int) : Call<Boolean>

    @GET("/boardDetail/detail")
    fun getBoardDetailWithComment(@Query("id") id: Int) : Call<BoardDetailWithCommentResponse>

    // 게시판 글 찜하기
    @POST("/boardDetail/like")
    fun likeBoard(@Query("boardDetailId") boardDetailId: Int, @Query("userId") userId: String) : Call<Any>

    // 찜하기 여부
    @POST("/boardDetail/isLike")
    fun isLikeBoard(@Query("boardDetailId") boardDetailId: Int, @Query("userId") userId: String) : Call<Boolean>

    @GET("/boardDetail/detail")
    fun getListBoardDetail(@Query("id") id:Int) : Call<Map<String,Any>>

    @PUT("boardDetail/update")
    fun modifyBoardDetail(@Body boardDetail:BoardDetail) : Call<Boolean>
}