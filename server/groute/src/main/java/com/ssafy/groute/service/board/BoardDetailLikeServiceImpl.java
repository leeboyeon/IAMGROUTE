package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.BoardDetailLike;
import com.ssafy.groute.mapper.board.BoardDetailLikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardDetailLikeServiceImpl implements BoardDetailLikeService{
    @Autowired
    BoardDetailLikeMapper boardDetailLikeMapper;
    @Override
    public BoardDetailLike findBoardLikeByUIdBDId(String userId, int boardDetailId) throws Exception {
        return boardDetailLikeMapper.findBoardLikeByUIdBDId(userId, boardDetailId);
    }

    @Override
    public void deleteBoardDetailLike(int id) throws Exception {
        boardDetailLikeMapper.deleteBoardDetailLike(id);
    }

    @Override
    public void insertBoardDetailLike(String userId, int boardDetailId) throws Exception {
        boardDetailLikeMapper.insertBoardDetailLike(userId, boardDetailId);
    }

    @Override
    public int findLikeByBDId(int boardDetailId) throws Exception {
        return boardDetailLikeMapper.findLikeByBDId(boardDetailId);
    }
}
