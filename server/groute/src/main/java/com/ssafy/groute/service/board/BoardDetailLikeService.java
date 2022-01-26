package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.BoardDetailLike;
import org.springframework.stereotype.Service;

@Service
public interface BoardDetailLikeService {
    BoardDetailLike findBoardLikeByUIdBDId(String userId, int boardDetailId) throws Exception;
    void deleteBoardDetailLike(int id) throws Exception;
    void insertBoardDetailLike(String userId, int boardDetailId) throws Exception;
    int findLikeByBDId(int boardDetailId) throws Exception;
}
