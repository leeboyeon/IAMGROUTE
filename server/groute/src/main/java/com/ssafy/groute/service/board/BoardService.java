package com.ssafy.groute.service.board;

import com.ssafy.groute.dto.board.Board;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BoardService {
    void insertBoard(Board board) throws Exception;
    Board selectBoard(int id) throws Exception;
    List<Board> selectAllBoard() throws Exception;
    void deleteBoard(int id) throws Exception;
    void updateBoard(Board board) throws Exception;
}
