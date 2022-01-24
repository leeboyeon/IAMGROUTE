package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.Board
import retrofit2.Call
import retrofit2.http.GET

interface BoardApi {

    // 자유게시판, 질문게시판 게시글 리스트 조회
    @GET("/boardDetail/list")
    fun listBoard() : Call<List<Board>>
}