package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.BoardDetail;
import com.ssafy.groute.dto.board.BoardDetailLike;
import com.ssafy.groute.mapper.board.BoardDetailLikeMapper;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardDetailLikeServiceImpl implements BoardDetailLikeService{
    @Autowired
    BoardDetailLikeMapper boardDetailLikeMapper;
    @Autowired
    BoardDetailMapper boardDetailMapper;
    @Override
    public BoardDetailLike findBoardLikeByUIdBDId(String userId, int boardDetailId) throws Exception {
        return boardDetailLikeMapper.findBoardLikeByUIdBDId(userId, boardDetailId);
    }

    @Override
    public void deleteBoardDetailLike(int id) throws Exception {
        int boardDetailId = boardDetailLikeMapper.findLikeById(id);
        boardDetailLikeMapper.deleteBoardDetailLike(id);
        BoardDetail res = boardDetailMapper.selectBoardDetail(boardDetailId);
        res.setHeartCnt(res.getHeartCnt()-1);
        boardDetailMapper.updateBoardDetailHitCntOrLike(res);

    }

    @Override
    public void insertBoardDetailLike(String userId, int boardDetailId) throws Exception {
        boardDetailLikeMapper.insertBoardDetailLike(userId, boardDetailId);
        BoardDetail res = boardDetailMapper.selectBoardDetail(boardDetailId);
        res.setHeartCnt(res.getHeartCnt()+1);
        boardDetailMapper.updateBoardDetailHitCntOrLike(res);
    }

    @Override
    public int findLikeByBDId(int boardDetailId) throws Exception {
        return boardDetailLikeMapper.findLikeByBDId(boardDetailId);
    }
}
