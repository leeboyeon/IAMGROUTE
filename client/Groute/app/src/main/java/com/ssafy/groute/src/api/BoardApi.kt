package com.ssafy.groute.src.api

import com.ssafy.groute.src.dto.BoardDetail
import com.ssafy.groute.src.response.BoardDetailWithCommentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BoardApi {

    // 자유게시판, 질문게시판 게시글 리스트 조회 -> 사용 X
    @GET("/boardDetail/list")
    fun listBoard() : Call<MutableList<BoardDetail>>

    // 게시판 타입에 따른 게시글 리스트 조회 - coroutine ver.
    @GET("/boardDetail/list/division")
    fun listBoardDetail(@Query("boardId") boardId : Int) : Call<MutableList<BoardDetail>>

    // 게시판 타입에 따른 게시글 리스트 조회 - coroutine ver.
    @GET("/boardDetail/list/division")
    suspend fun getBoardPostList(@Query("boardId") boardId : Int) : Response<List<BoardDetail>>

    /**
     * #S06P12D109-189
     * 게시판 글쓰기 + img upload
     */
    @Multipart
    @POST("/boardDetail/insert")
    fun insertBoardDetail(@Part("board") board : RequestBody, @Part img : MultipartBody.Part?) : Call<Int>

    /**
     * #S06P12D109-253
     * 게시글 수정
     */
    @Multipart
    @PUT("boardDetail/update")
    fun modifyBoardDetail(@Part("board") board : RequestBody, @Part img : MultipartBody.Part?) : Call<Int>

    // 게시판 글삭제
    @DELETE("/boardDetail/del")
    fun deleteBoardDetail(@Query("id") id: Int) : Call<Boolean>

    // id에 해당하는 게시글과 댓글 조회
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

    // id에 해당하는 게시글과 댓글 조회 - coroutine ver.
    @GET("/boardDetail/detail")
    suspend fun getBoardDetailWithCmtByCor(@Query("id") id: Int) : Response<BoardDetailWithCommentResponse>

}