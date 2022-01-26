package com.ssafy.groute.mapper.board;

import com.ssafy.groute.dto.board.BoardDetailLike;

public interface BoardDetailLikeMapper {
    BoardDetailLike findBoardLikeByUIdBoardDetailId(String userId, int boardDetailId) throws Exception;
    void deleteBoardDetailLike(int id) throws Exception;
    void insertBoardDetailLike(String userId, int boardDetailId) throws Exception;
    int findLikeByBDId(int boardDetailId) throws Exception;
    int findLikeById(int id) throws Exception;
    void deleteAllBoardDetailLikeByBoardDetailId(int boardDetailId) throws Exception;
    void deleteAllBoardDetailLikeByUId(String userId) throws Exception;
}
