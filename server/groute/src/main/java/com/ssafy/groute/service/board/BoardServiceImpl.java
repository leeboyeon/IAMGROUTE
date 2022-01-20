package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.Board;
import com.ssafy.groute.dto.board.Board;
import com.ssafy.groute.mapper.board.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService{
    @Autowired
    BoardMapper boardMapper;

    @Override
    public void insertBoard(Board board) throws Exception {
        boardMapper.insertBoard(board);
    }

    @Override
    public Board selectBoard(int id) throws Exception {
        return boardMapper.selectBoard(id);
    }

    @Override
    public List<Board> selectAllBoard() throws Exception {
        return boardMapper.selectAllBoard();
    }

    @Override
    public void deleteBoard(int id) throws Exception {
        boardMapper.deleteBoard(id);
    }

    @Override
    public void updateBoard(Board board) throws Exception {
        boardMapper.updateBoard(board);
    }
}
