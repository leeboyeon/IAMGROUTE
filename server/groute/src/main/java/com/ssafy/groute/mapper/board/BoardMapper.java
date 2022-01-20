package com.ssafy.groute.mapper.board;

import com.ssafy.groute.dto.board.Board;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface BoardMapper {
    void insertBoard(Board board) throws Exception;
    Board selectBoard(int id) throws Exception;
    List<Board> selectAllBoard() throws Exception;
    void deleteBoard(int id) throws Exception;
    void updateBoard(Board board) throws Exception;
}
