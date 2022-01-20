package com.ssafy.groute.mapper.board;
import com.ssafy.groute.dto.board.BoardDetail;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface BoardDetailMapper {
    void insertBoardDetail(BoardDetail boardDetail) throws Exception;
    BoardDetail selectBoardDetail(int id) throws Exception;
    List<BoardDetail> selectAllBoardDetail() throws Exception;
    void deleteBoardDetail(int id) throws Exception;
    void updateBoardDetail(BoardDetail boardDetail) throws Exception;
}
