package com.ssafy.groute.service.board;


import com.ssafy.groute.dto.board.BoardDetail;

import com.ssafy.groute.mapper.board.BoardDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardDetailServiceImpl implements BoardDetailService {
    @Autowired
    BoardDetailMapper boardDetailMapper;

    @Override
    public void insertBoardDetail(BoardDetail boardDetail) throws Exception {
        boardDetailMapper.insertBoardDetail(boardDetail);
    }

    @Override
    public BoardDetail selectBoardDetail(int id) throws Exception {
        BoardDetail res = boardDetailMapper.selectBoardDetail(id);
        res.setHitCnt(res.getHitCnt()+1);
        boardDetailMapper.updateBoardDetail(res);
        return res;
    }

    @Override
    public List<BoardDetail> selectAllBoardDetail() throws Exception {
        return boardDetailMapper.selectAllBoardDetail();
    }

    @Override
    public void deleteBoardDetail(int id) throws Exception {
        boardDetailMapper.deleteBoardDetail(id);
    }

    @Override
    public void updateBoardDetail(BoardDetail boardDetail) throws Exception {
        boardDetailMapper.updateBoardDetail(boardDetail);
    }
}
