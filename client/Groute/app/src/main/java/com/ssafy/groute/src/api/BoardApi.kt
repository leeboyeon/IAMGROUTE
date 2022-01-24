package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.BoardDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
<<<<<<< HEAD
import retrofit2.http.Query
=======
import retrofit2.http.POST
>>>>>>> feature/BoardF_create

interface BoardApi {

    // 자유게시판, 질문게시판 게시글 리스트 조회
    @GET("/boardDetail/list")
    fun listBoard() : Call<MutableList<BoardDetail>>

<<<<<<< HEAD
    // 게시판 타입에 따른 리스트 조회
    @GET("/boardDetail/list/division")
    fun listBoardDetail(@Query("boardId") boardId : Int) : Call<MutableList<BoardDetail>>
=======
    @POST("/boardDetail/insert")
    fun insertBoardDetail(@Body boardDetail:BoardDetail) : Call<Boolean>
>>>>>>> feature/BoardF_create
}