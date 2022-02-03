package com.ssafy.groute.service.board;


import com.ssafy.groute.dto.board.BoardDetail;

import com.ssafy.groute.dto.board.BoardDetailLike;
import com.ssafy.groute.mapper.board.BoardDetailLikeMapper;
import com.ssafy.groute.mapper.board.BoardDetailMapper;
import com.ssafy.groute.mapper.board.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardDetailServiceImpl implements BoardDetailService {
    @Autowired
    BoardDetailMapper boardDetailMapper;
    @Autowired
    BoardDetailLikeMapper boardDetailLikeMapper;
    @Autowired
    CommentMapper commentMapper;

    @Override
    public void insertBoardDetail(BoardDetail boardDetail) throws Exception {
        boardDetailMapper.insertBoardDetail(boardDetail);
    }

    @Override
    public BoardDetail selectBoardDetail(int id) throws Exception {
        BoardDetail res = boardDetailMapper.selectBoardDetail(id);
        res.setHitCnt(res.getHitCnt()+1);
        boardDetailMapper.updateBoardDetailHitCntOrLikeOrCommentCnt(res);
        return res;
    }

    @Override
    public List<BoardDetail> selectAllBoardDetail() throws Exception {
        return boardDetailMapper.selectAllBoardDetail();
    }

    @Override
    @Transactional
    public void deleteBoardDetail(int boardDetailId) throws Exception {
        boardDetailLikeMapper.deleteAllBoardDetailLikeByBoardDetailId(boardDetailId);
        commentMapper.deleteAllCommentByBoardDetailId(boardDetailId);
        boardDetailMapper.deleteBoardDetail(boardDetailId);
    }

    @Override
    public void updateBoardDetail(BoardDetail boardDetail) throws Exception {
        boardDetailMapper.updateBoardDetail(boardDetail);
    }

    @Override
    public List<BoardDetail> selectBoardDetailSeparetedByTag(int boardId) throws Exception {
        return boardDetailMapper.selectBoardDetailSeparetedByTag(boardId);
    }
}
