package com.ssafy.groute.mapper.board;

import com.ssafy.groute.dto.board.BoardDetailLike;

public interface BoardDetailLikeMapper {
    BoardDetailLike findBoardLikeByUIdBDId(String userId, int boardDetailId) throws Exception;
    void deleteBoardDetailLike(int id) throws Exception;
    void insertBoardDetailLike(String userId, int boardDetailId) throws Exception;
    int findLikeByBDId(int boardDetailId) throws Exception;
}
